package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService implements at.ac.tuwien.sepm.groupphase.backend.service.CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CategoryRepository categoryRepository;
    private final Validator validator;

    public CategoryService(CategoryRepository categoryRepository, Validator validator) {
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public Category saveCategory(Category category) {
        LOGGER.debug("Save new category");
        category.setId(null);
        validator.validateNewCategory(category);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        LOGGER.debug("Delete category with id {}", id);
        validator.validateId(id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) categoryRepository.deleteById(id);
        else throw new NotFoundException(String.format("Could not find category with id %s", id));
    }

    @Override
    public Category findOne(Long id) {
        LOGGER.debug("Get category with id {}", id);
        validator.validateId(id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) return category.get();
        else throw new NotFoundException(String.format("Could not find category with id %s", id));
    }

    @Override
    public List<Category> findAll() {
        LOGGER.debug("Get all saved categories");
        List<Category> allFoods = categoryRepository.findAll();
        return categoryRepository.findAll();
    }

    @Override
    public Category patch(Category category) {
        LOGGER.debug("Patch category");
        validator.validateCategoryPatch(category);
        Long id = category.getId();
        if (categoryRepository.findById(id).isPresent()){
            categoryRepository.save(category);
        } else {
            throw new NotFoundException(String.format("Could not find category with id %s", id));
        }
        return category;
    }

    public List<Category> getAllFoodCategories() {
        LOGGER.debug("Get all food categories");
        List<Category> allCategories = findAll();
        List<Category> foodCategories = new ArrayList<>();
        if (!allCategories.isEmpty()) {
            for (int i = 0; i < allCategories.size(); i++) {
                Category curr = allCategories.get(i);
                if (curr.getAssignedFood() != null && !curr.getAssignedFood().isEmpty()) {
                    foodCategories.add(curr);
                }
            }
        }
        return foodCategories;
    }

    @Override
    public List<Category> getAllDrinkCategories() {
        LOGGER.debug("Get all drink categories");
        List<Category> allCategories = findAll();
        List<Category> drinkCategories = new ArrayList<>();
        if (!allCategories.isEmpty()) {
            for (int i = 0; i < allCategories.size(); i++) {
                Category curr = allCategories.get(i);
                if (curr.getAssignedDrink() != null && !curr.getAssignedDrink().isEmpty()) {
                    drinkCategories.add(curr);
                }
            }
        }
        return drinkCategories;
    }

    public List<Category> findAssignedToFood(Long id) {
        LOGGER.debug("Get all food categories assinged to food with id {}, that have set show-flag.", id);

        List<Category> shownCategories = categoryRepository.findByShow(true);
        List<Category> matchingCategories = new ArrayList<>();
        if (!shownCategories.isEmpty()) {
            for (Category cat: shownCategories) {
                //check, if assigned to food with given id
                //get food, the category is assigned to
                Set<Food> assignedToFood = cat.getAssignedFood();
                //loop food and check id
                for (Food food: assignedToFood){
                    if (food.getId().equals(id)) {
                        matchingCategories.add(cat);
                        break;
                    }
                }
            }
        }

        return matchingCategories;
    }

    public List<Category> findAssignedToDrink(Long id) {
        LOGGER.debug("Get all drink categories assinged to drink with id {}, that have set show-flag.", id);

        List<Category> shownCategories = categoryRepository.findByShow(true);
        List<Category> matchingCategories = new ArrayList<>();
        if (!shownCategories.isEmpty()) {
            for (Category cat: shownCategories) {
                //check, if assigned to drink with given id
                //get drink, the category is assigned to
                Set<Drink> assignedToDrink = cat.getAssignedDrink();
                //loop drinks and check id
                for (Drink drink: assignedToDrink){
                    if (drink.getId().equals(id)) {
                        matchingCategories.add(cat);
                        break;
                    }
                }
            }
        }

        return matchingCategories;
    }
}
