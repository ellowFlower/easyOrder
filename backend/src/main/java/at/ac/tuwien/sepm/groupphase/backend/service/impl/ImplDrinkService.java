package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DrinkRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DrinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import at.ac.tuwien.sepm.groupphase.backend.validation.Validator;
import java.lang.invoke.MethodHandles;
import java.util.*;

@Service
public class ImplDrinkService implements DrinkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DrinkRepository drinkRepository;
    private final CategoryRepository categoryRepository;
    private final Validator validator;

    public ImplDrinkService(DrinkRepository drinkRepository, CategoryRepository categoryRepository, Validator validator) {
        this.drinkRepository = drinkRepository;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public Drink saveDrink(Drink drink) {
        LOGGER.debug("Save new drink");

        //if ID is given, it is set to null, so that no existing entity can be overwritten
        drink.setId(null);
        validator.validateNewDrink(drink);
        Drink savedDrink = drinkRepository.save(drink);

        //update categories by adding assigned drink
        List<Category> categories = drink.getAssignedCategories();
        for (Category cat: categories){
            Set<Drink> drinkSet = cat.getAssignedDrink();
            if (drinkSet == null){ drinkSet = new HashSet<>(); }
            drinkSet.add(drink);
            cat.setAssignedDrink(drinkSet);
            categoryRepository.save(cat);
        }

        return drink;
    }

    @Override
    public Drink assignCategory(Long drinkId, Long categoryId) {
        LOGGER.debug("Assign drink to a category");
        validator.validateId(drinkId);
        validator.validateId(categoryId);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Drink> optionalDrink = drinkRepository.findById(drinkId);

        if (optionalCategory.isPresent() && optionalDrink.isPresent()) {
            Category category = optionalCategory.get();
            Drink drink = optionalDrink.get();
            category.getAssignedDrink().add(drink);
            drink.getAssignedCategories().add(category);
            categoryRepository.save(category);
            return drinkRepository.save(drink);
        } else {
            throw new ServiceException("Drink with id: " + drinkId + " can not be assigned to category with id: " + categoryId);
        }
    }

    @Override
    public Drink removeCategory(Long drinkId, Long categoryId) {
        LOGGER.debug("Remove a drink from a category");
        validator.validateId(drinkId);
        validator.validateId(categoryId);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Drink> optionalDrink = drinkRepository.findById(drinkId);

        if (optionalCategory.isPresent() && optionalDrink.isPresent()) {
            Category category = optionalCategory.get();
            Drink drink = optionalDrink.get();
            category.getAssignedDrink().remove(drink);
            drink.getAssignedCategories().remove(category);
            categoryRepository.save(category);
            return drinkRepository.save(drink);
        } else {
            throw new ServiceException("Drink with id: " + drinkId + " can not be removed from category with id: " + categoryId);
        }
    }

    public void deleteDrink(Long id) {
        LOGGER.debug("Delete drink with id {}", id);
        validator.validateId(id);
        Optional<Drink> searchedDrink = drinkRepository.findById(id);
        if (searchedDrink.isEmpty()) throw new NotFoundException(String.format("Could not find drink with id %s", id));
        Drink foundDrink = searchedDrink.get();
        List<Category> catSet = foundDrink.getAssignedCategories();
        for (Category cat: catSet){
            Set<Drink> s = cat.getAssignedDrink();
            s.remove(foundDrink);
            cat.setAssignedDrink(s);
            categoryRepository.save(cat);
        }

        drinkRepository.deleteById(id);
    }

    @Override
    public Drink findOne(Long id) {
        LOGGER.debug("Get drink with id {}", id);
        validator.validateId(id);
        Optional<Drink> drink = drinkRepository.findById(id);
        if (drink.isPresent()) return drink.get();
        else throw new NotFoundException(String.format("Could not find drink with id %s", id));
    }

    @Override
    public List<Drink> findAll() {
        LOGGER.debug("Get all saved drinks");
        return drinkRepository.findByDeletedAndUpdated(false, false);
    }

    @Override
    public List<Drink> getAllWithDeleted() {
        LOGGER.debug("Get all deleted drinks");
        return drinkRepository.findByDeleted(true);
    }

    @Override
    public List<Drink> getAllWithUpdated() {
        LOGGER.debug("Get all updated drinks");
        return drinkRepository.findByUpdated(true);
    }

    @Override
    public Drink patch(Drink drink) {
        LOGGER.debug("Patch drink");
        validator.validateDrinkPatch(drink);
        Long id = drink.getId();
        Optional<Drink> foundDrink = drinkRepository.findById(id);

        if (foundDrink.isEmpty()) {
            throw new NotFoundException(String.format("Could not find drink with id %s", id));
        }
        Drink drinkToPatch = foundDrink.get();
        Drink patchedDrink = drinkRepository.save(drink);

        List<Category> newCategories = drink.getAssignedCategories();
        List<Category> oldCategories = drinkToPatch.getAssignedCategories();
        List<Category> sameCategories = new ArrayList<>(newCategories);
        sameCategories.retainAll(oldCategories);

        //remove all cats in the two lists, that are the same
        newCategories.removeAll(sameCategories);
        oldCategories.removeAll(sameCategories);

        //are there cats to save?
        //are there cats in new, that are not in old?
        //every cat left in new must be saved
        for (Category cat: newCategories){
            Set<Drink> drinkSet = cat.getAssignedDrink();
            if (drinkSet == null){ drinkSet = new HashSet<>(); }
            drinkSet.add(drink);
            cat.setAssignedDrink(drinkSet);
            categoryRepository.save(cat);
        }

        //are there cats to delete?
        //are there cats in old, that are not in new?
        //every cat left in old must be deleted
        for (Category cat: oldCategories){
            Set<Drink> drinkSet = cat.getAssignedDrink();
            drinkSet.remove(drinkToPatch);
            cat.setAssignedDrink(drinkSet);
            categoryRepository.save(cat);
        }

        return patchedDrink;
    }

    @Override
    public List<Drink> findDrinksByName(String name) {
        LOGGER.debug("Get drinks by name");
        return drinkRepository.findAllByNameContaining(name.toLowerCase());
    }
}
