package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;

import java.util.List;

public interface DrinkService {

    /**
     * Save a single drink
     *
     * @param drink to save
     * @return saved drink entry
     */
    Drink saveDrink(Drink drink);

    /**
     * Assert a single drink to a category
     * @param drinkId of the drink which is used
     * @param categoryId of the category to assign
     * @return the now assigned drink
     */
    Drink assignCategory(Long drinkId, Long categoryId);

    Drink removeCategory(Long drinkId, Long categoryId);

    /**
     * delete a specific drink
     *
     * @param id of the drink to be deleted
     */
    void deleteDrink(Long id);

    /**
     * get a drink by its ID
     *
     * @param id to be returned
     * @return drink with specified id
     */
    Drink findOne(Long id);

    /**
     * get all saved drinks.
     *
     * @return drink with specified id
     */
    List<Drink> findAll();

    /**
     * update a drink entity
     *
     * @param drink containing the new values
     * @return drink after the patch
     */
    Drink patch(Drink drink);

    /**
     * get all drinks which are deleted.
     *
     * @return list of drink entities
     */
    List<Drink> getAllWithDeleted();

    /**
     * get all drinks which are updated.
     *
     * @return list of drink entities
     */
    List<Drink> getAllWithUpdated();

    /**
     * find drinks by name
     *
     * @param name of the drinks to be find
     * @return founded drinks
     */
    List<Drink> findDrinksByName(String name);
}
