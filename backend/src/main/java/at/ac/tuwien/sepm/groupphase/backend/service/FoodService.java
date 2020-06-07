package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Food;

import java.util.List;

public interface FoodService {

    /**
     * Save a single food
     *
     * @param food to save
     * @return saved food entry
     */
    Food saveFood(Food food);

    /**
     * Assert a single food to a category
     * @param foodId of the food which is used
     * @param categoryId of the category to assign
     * @return the now assigned food
     */
    Food assignCategory(Long foodId, Long categoryId);

    Food removeCategory(Long foodId, Long categoryId);

    /**
     * delete a specific food
     *
     * @param id of the food to be deleted
     */
    void deleteFood(Long id);

    /**
     * get a food by its ID
     *
     * @param id to be returned
     * @return food with specified id
     */
    Food findOne(Long id);

    /**
     * get all saved foods.
     *
     * @return food with specified id
     */
    List<Food> findAll();

    /**
     * get all foods which are deleted.
     *
     * @return list of food entities
     */
    List<Food> getAllWithDeleted();

    /**
     * get all foods which are updated.
     *
     * @return list of food entities
     */
    List<Food> getAllWithUpdated();

    /**
     * update a food entity
     *
     * @param food containing the new values
     * @return food after the patch
     */
    Food patch(Food food);


    /**
     * find foods by name
     *
     * @param name of the foods to be find
     * @return founded foods
     */
    List<Food> findFoodsByName(String name);
}
