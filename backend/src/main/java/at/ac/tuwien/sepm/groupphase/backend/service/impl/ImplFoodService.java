package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.FoodRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import at.ac.tuwien.sepm.groupphase.backend.validation.Validator;
import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class ImplFoodService implements FoodService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final Validator validator;

    public ImplFoodService(FoodRepository foodRepository, CategoryRepository categoryRepository, Validator validator) {
        this.foodRepository = foodRepository;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public Food saveFood(Food food) {
        LOGGER.debug("Save new food");

        //if ID is given, it is set to null, so that no existing entity can be overwritten
        food.setId(null);
        validator.validateNewFood(food);
        Food savedFood = foodRepository.save(food);

        //update categories by adding assigned food
        List<Category> categories = food.getCategories();
        for (Category cat: categories){
            Set<Food> foodSet = cat.getAssignedFood();
            if (foodSet == null){ foodSet = new HashSet<>(); }
            foodSet.add(food);
            cat.setAssignedFood(foodSet);
            categoryRepository.save(cat);
        }

        return savedFood;
    }

    @Override
    public Food assignCategory(Long foodId, Long categoryId) {
        LOGGER.debug("Assign food to a category");
        validator.validateId(foodId);
        validator.validateId(categoryId);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Food> optionalFood = foodRepository.findById(foodId);

        if (optionalCategory.isPresent() && optionalFood.isPresent()) {
            Category category = optionalCategory.get();
            Food food = optionalFood.get();
            category.getAssignedFood().add(food);
            food.getCategories().add(category);
            categoryRepository.save(category);
            return foodRepository.save(food);
        } else {
            throw new ServiceException("Food with id: " + foodId + " can not be assigned to category with id: " + categoryId);
        }
    }

    @Override
    public Food removeCategory(Long foodId, Long categoryId) {
        LOGGER.debug("Remove food from a category");
        validator.validateId(foodId);
        validator.validateId(categoryId);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Food> optionalFood = foodRepository.findById(foodId);

        if (optionalCategory.isPresent() && optionalFood.isPresent()) {
            Category category = optionalCategory.get();
            Food food = optionalFood.get();
            category.getAssignedFood().remove(food);
            food.getCategories().remove(category);
            categoryRepository.save(category);
            return foodRepository.save(food);
        } else {
            throw new ServiceException("Food with id: " + foodId + " can not be removed from category with id: " + categoryId);
        }
    }

    public void deleteFood(Long id) {
        LOGGER.debug("Delete food with id {}", id);
        validator.validateId(id);
        Optional<Food> searchedFood = foodRepository.findById(id);
        if (searchedFood.isEmpty()) throw new NotFoundException(String.format("Could not find food with id %s", id));
        Food foundFood = searchedFood.get();
        List<Category> catSet = foundFood.getCategories();
        for (Category cat: catSet){
            Set<Food> s = cat.getAssignedFood();
            s.remove(foundFood);
            cat.setAssignedFood(s);
            categoryRepository.save(cat);
        }

        foodRepository.deleteById(id);
    }

    @Override
    public Food findOne(Long id) {
        LOGGER.debug("Get food with id {}", id);
        validator.validateId(id);
        Optional<Food> food = foodRepository.findById(id);
        if (food.isPresent()) return food.get();
        else throw new NotFoundException(String.format("Could not find food with id %s", id));
    }

    @Override
    public List<Food> findAll() {
        LOGGER.debug("Get all saved foods");
        return foodRepository.findByDeletedAndUpdated(false, false);
    }

    @Override
    public List<Food> getAllWithDeleted() {
        LOGGER.debug("Get all deleted foods");
        return foodRepository.findByDeleted(true);
    }

    @Override
    public List<Food> getAllWithUpdated() {
        LOGGER.debug("Get all updated foods");
        return foodRepository.findByUpdated(true);
    }

    @Override
    public Food patch(Food food) {
        LOGGER.debug("Patch food");
        validator.validateFoodPatch(food);
        Long id = food.getId();
        Optional<Food> foundFood = foodRepository.findById(id);

        if (foundFood.isEmpty()) {
            throw new NotFoundException(String.format("Could not find food with id %s", id));
        }
        Food foodToPatch = foundFood.get();
        Food patchedFood = foodRepository.save(food);

        List<Category> newCategories = food.getCategories();
        List<Category> oldCategories = foodToPatch.getCategories();
        List<Category> sameCategories = new ArrayList<>(newCategories);
        sameCategories.retainAll(oldCategories);

        //remove all cats in the two lists, that are the same
        newCategories.removeAll(sameCategories);
        oldCategories.removeAll(sameCategories);

        //are there cats to save?
        //are there cats in new, that are not in old?
        //every cat left in new must be saved
        for (Category cat: newCategories){
            Set<Food> foodSet = cat.getAssignedFood();
            if (foodSet == null){ foodSet = new HashSet<>(); }
            foodSet.add(food);
            cat.setAssignedFood(foodSet);
            categoryRepository.save(cat);
        }

        //are there cats to delete?
        //are there cats in old, that are not in new?
        //every cat left in old must be deleted
        for (Category cat: oldCategories){
            Set<Food> foodSet = cat.getAssignedFood();
            foodSet.remove(foodToPatch);
            cat.setAssignedFood(foodSet);
            categoryRepository.save(cat);
        }

        return patchedFood;
    }

    @Override
    public List<Food> findFoodsByName(String name) {
        LOGGER.debug("Get foods by name");
        return foodRepository.findAllByNameContaining(name.toLowerCase());
    }
}