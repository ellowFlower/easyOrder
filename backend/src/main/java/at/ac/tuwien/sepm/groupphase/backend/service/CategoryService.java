package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    /**
     * Save a single category
     *
     * @param category to save
     * @return saved category entry
     */
    Category saveCategory(Category category);

    /**
     * delete a specific category
     *
     * @param id of the category to be deleted
     * @return deleted category entry
     */
    void deleteCategory(Long id);

    /**
     * get a category by its ID
     *
     * @param id to be returned
     * @return category with specified id
     */
    Category findOne(Long id);

    /**
     * get all saved categories.
     *
     * @return all categories
     */
    List<Category> findAll();

    /**
     * update a category entity
     *
     * @param category containing the new values
     * @return category after the patch
     */
    Category patch(Category category);

    /**
     * get all categories where foods are assigned
     * @return a list with all the categories
     */
    List<Category> getAllFoodCategories();

    /**
     * get all categories where drinks are assigned
     * @return a list with all the categories
     */
    List<Category> getAllDrinkCategories();

    /**
     * get all categories assigned to food with id, that have set show-flag
     * @param id the id of the food, for which assigned categories are searched for
     * @return a list with categories matching the described criteria
     */
    List<Category> findAssignedToFood(Long id);

    /**
     * get all categories assigned to drink with id, that have set show-flag
     * @param id the id of the drink, for which assigned categories are searched for
     * @return a list with categories matching the described criteria
     */
    List<Category> findAssignedToDrink(Long id);
}
