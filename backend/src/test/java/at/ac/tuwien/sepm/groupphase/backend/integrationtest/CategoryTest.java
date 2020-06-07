package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FoodRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CategoryService;
import at.ac.tuwien.sepm.groupphase.backend.service.DrinkService;
import at.ac.tuwien.sepm.groupphase.backend.service.FoodService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryTest implements TestData {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    private Category category;
    private Category category2;
    private Category category3;
    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodRepository foodRepository;
    private Food food;
    private Food food2;

@Autowired
    private DrinkService drinkService;
    private Drink drink;
    private Drink drink2;



@BeforeEach
    public void beforeEach() {
        food = Food.FoodBuilder.aFood()
            .withId(1L)
            .withName("Testname1")
            .withDescription("Desc")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .build();

        food2 = Food.FoodBuilder.aFood()
            .withId(2L)
            .withName("Testname2")
            .withDescription("Desc")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .build();

        drink = Drink.DrinkBuilder.aDrink()
            .withId(1L)
            .withName("TestDrink")
            .withDescription("")
            .withPrice(new BigDecimal("10.00"))
            .withAlcohol(10)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        drink2 = Drink.DrinkBuilder.aDrink()
            .withId(2L)
            .withName("TestDrink2")
            .withDescription("")
            .withPrice(new BigDecimal("10.00"))
            .withAlcohol(10)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        category = Category.CategoryBuilder.aCategory()
            .withId(1L)
            .withName("TestCategory")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        category2 = Category.CategoryBuilder.aCategory()
            .withId(2L)
            .withName("TestCategory2")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        category3 = Category.CategoryBuilder.aCategory()
            .withId(3L)
            .withName("TestCategory3")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();
    }

    @AfterEach
    public void afterEach() {
        try {
            List<Drink> drinks = drinkService.findAll();
            for (Drink drink : drinks) {
                Long id = drink.getId();
                drinkService.deleteDrink(id);
            }
            List<Food> foods = foodService.findAll();
            for (Food food : foods) {
                Long id = food.getId();
                foodService.deleteFood(id);
            }
            List<Category> categories = categoryService.findAll();
            for (Category category : categories) {
                Long id = category.getId();
                categoryService.deleteCategory(id);
            }
        } catch (NotFoundException ignored) {
        }
   }


    @Test
    public void adhoc_updateAssignedCats_addACat() throws Exception {
        category = categoryRepository.save(category);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        food.setCategories(catList);
        food = foodService.saveFood(food);

        category2 = categoryRepository.save(category2);
        catList.add(category2);
        food.setCategories(catList);
        food = foodService.patch(food);

        assertEquals(2, food.getCategories().size());
        category = categoryService.findOne(category.getId());
        category2 = categoryService.findOne(category2.getId());

        Set<Food> foodSet1 = category.getAssignedFood();
        Set<Food> foodSet2 = category2.getAssignedFood();

        assertTrue(foodSet1.contains(food));
        assertTrue(foodSet2.contains(food));

    }

    @Test
    public void adhoc_updateAssignedCats_removeACat() throws Exception {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        food.setCategories(catList);
        food = foodService.saveFood(food);

        catList.remove(category2);
        food.setCategories(catList);
        food = foodService.patch(food);

        assertEquals(1, food.getCategories().size());
        category = categoryService.findOne(category.getId());
        category2 = categoryService.findOne(category2.getId());

        Set<Food> foodSet1 = category.getAssignedFood();
        Set<Food> foodSet2 = category2.getAssignedFood();

        assertTrue(foodSet1.contains(food));
        assertFalse(foodSet2.contains(food));
    }

    //-----------------------------------------------------------------------------

    @Test
    public void adhoc1_findAssignedToFood_notShownFoodsNotReturned() throws Exception {
        category = categoryRepository.save(category);
        category2.setShow(false);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        food.setCategories(catList);
        food = foodService.saveFood(food);

        catList = categoryService.findAssignedToFood(food.getId());
        assertEquals(1, catList.size());
        assertTrue(catList.contains(category));
        assertFalse(catList.contains(category2));
    }

    @Test
    public void adhoc2_findAssignedToFood_NoCatsExist() {
        List<Category> catList = new ArrayList<>();
        food.setCategories(catList);
        food = foodService.saveFood(food);
        catList = categoryService.findAssignedToFood(food.getId());

        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc3_findAssignedToFood_CatsExistNoneAssigned() {
        List<Category> catList = new ArrayList<>();
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        food.setCategories(catList);
        food = foodService.saveFood(food);
        catList = categoryService.findAssignedToFood(food.getId());

        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc4_findAssignedToFood_OnlyAssignedToOtherFood() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        food.setCategories(catList);
        food = foodService.saveFood(food);
        catList.add(category);
        catList.add(category2);
        food2.setCategories(catList);
        food2 = foodService.saveFood(food2);
        catList = categoryService.findAssignedToFood(food.getId());

        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc5_findAssignedToFood_OneAssignedNoneAssignedToOthers() {
        category = categoryRepository.save(category);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        food.setCategories(catList);
        food = foodService.saveFood(food);

        catList = categoryService.findAssignedToFood(food.getId());
        assertEquals(1, catList.size());

        Category foundCat;
        foundCat = catList.get(0);
        assertTrue(category.equals(foundCat));
    }

    @Test
    public void adhoc6_findAssignedToFood_OneAssignedSomeAssignedToOthers() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        food.setCategories(catList);
        food = foodService.saveFood(food);
        catList.add(category2);
        food2.setCategories(catList);
        food2 = foodService.saveFood(food2);

        catList = categoryService.findAssignedToFood(food.getId());
        assertEquals(1, catList.size());

        Category foundCat;
        foundCat = catList.get(0);
        assertTrue(category.equals(foundCat));
    }

    @Test
    public void adhoc7_findAssignedToFood_SomeAssignedNoneAssignedToOthers() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        food.setCategories(catList);
        food = foodService.saveFood(food);

        catList = categoryService.findAssignedToFood(food.getId());
        assertEquals(2, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
    }

    @Test
    public void adhoc8_findAssignedToFood_SomeAssignedSomeAssignedToOthers() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        food.setCategories(catList);
        food = foodService.saveFood(food);
        food2.setCategories(catList);
        food2 = foodService.saveFood(food2);

        catList = categoryService.findAssignedToFood(food.getId());
        assertEquals(2, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
    }

    @Test
    public void adhoc9_findAssignedToDrink_NoCatsExist() {
        List<Category> catList = new ArrayList<>();
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);
        catList = categoryService.findAssignedToDrink(drink.getId());

        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc8_findAssignedToDrink_CatsExistNoneAssigned() {
        List<Category> catList = new ArrayList<>();
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);
        catList = categoryService.findAssignedToDrink(drink.getId());

        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc9_findAssignedToDrink_OnlyAssignedToOtherDrink() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);
        catList.add(category);
        catList.add(category2);
        drink2.setAssignedCategories(catList);
        drink2 = drinkService.saveDrink(drink2);
        catList = categoryService.findAssignedToDrink(drink.getId());

        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc10_findAssignedToDrink_OneAssignedNoneAssignedToOthers() {
        category = categoryRepository.save(category);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);

        catList = categoryService.findAssignedToDrink(drink.getId());
        assertEquals(1, catList.size());

        Category foundCat;
        foundCat = catList.get(0);
        assertTrue(category.equals(foundCat));
    }

    @Test
    public void adhoc10_findAssignedToDrink_OneAssignedSomeAssignedToOthers() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);
        catList.add(category2);
        drink2.setAssignedCategories(catList);
        drink2 = drinkService.saveDrink(drink2);

        catList = categoryService.findAssignedToDrink(drink.getId());
        assertEquals(1, catList.size());

        Category foundCat;
        foundCat = catList.get(0);
        assertTrue(category.equals(foundCat));
    }

    @Test
    public void adhoc11_findAssignedToDrink_SomeAssignedNoneAssignedToOthers() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);

        catList = categoryService.findAssignedToDrink(drink.getId());
        assertEquals(2, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
    }

    @Test
    public void adhoc12_findAssignedToDrink_SomeAssignedSomeAssignedToOthers() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);
        drink2.setAssignedCategories(catList);
        drink2 = drinkService.saveDrink(drink2);

        catList = categoryService.findAssignedToDrink(drink.getId());
        assertEquals(2, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
    }

    @Test
    public void adhoc13_findAllAssignedToFood_NoneExist() {
        List<Category> catList = new ArrayList<>();
        food.setCategories(catList);
        food = foodService.saveFood(food);

        catList = categoryService.getAllFoodCategories();
        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc14_findAllAssignedToFood_NoneAssigned() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        food.setCategories(catList);
        food = foodService.saveFood(food);

        catList = categoryService.getAllFoodCategories();
        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc15_findAllAssignedToFood_AssignedToOneFood() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        food.setCategories(catList);
        food = foodService.saveFood(food);

        catList = categoryService.getAllFoodCategories();
        assertEquals(2, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
    }

    @Test
    public void adhoc16_findAllAssignedToFood_AssignedToMultipleFood() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        category3 = categoryRepository.save(category3);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        food.setCategories(catList);
        food = foodService.saveFood(food);
        catList.add(category3);
        food2.setCategories(catList);
        food2 = foodService.saveFood(food2);

        catList = categoryService.getAllFoodCategories();
        assertEquals(3, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
        assertTrue(catList.contains(category3));
    }

    @Test
    public void adhoc13_findAllAssignedToDrink_NoneExist() {
        List<Category> catList = new ArrayList<>();
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);

        catList = categoryService.getAllDrinkCategories();
        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc14_findAllAssignedToDrink_NoneAssigned() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);

        catList = categoryService.getAllDrinkCategories();
        assertEquals(0, catList.size());
    }

    @Test
    public void adhoc15_findAllAssignedToDrink_AssignedToOneDrink() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);

        catList = categoryService.getAllDrinkCategories();
        assertEquals(2, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
    }

    @Test
    public void adhoc16_findAllAssignedToDrink_AssignedToMultipleDrink() {
        category = categoryRepository.save(category);
        category2 = categoryRepository.save(category2);
        category3 = categoryRepository.save(category3);
        List<Category> catList = new ArrayList<>();
        catList.add(category);
        catList.add(category2);
        drink.setAssignedCategories(catList);
        drink = drinkService.saveDrink(drink);
        catList.add(category3);
        drink2.setAssignedCategories(catList);
        drink2 = drinkService.saveDrink(drink2);

        catList = categoryService.getAllDrinkCategories();
        assertEquals(3, catList.size());
        assertTrue(catList.contains(category));
        assertTrue(catList.contains(category2));
        assertTrue(catList.contains(category3));
    }

   /*
    @Test
    public void F1_assignedCategory_foodReturned() throws Exception {
        food = foodService.saveFood(food);
        category = categoryService.saveCategory(category);
        food = foodService.assignCategory(food.getId(), category.getId());
        assertEquals(1, food.getCategories().size());
        category = categoryService.findOne(category.getId());
        assertFalse(category.getAssignedFood().isEmpty());

    }

    @Test
    public void F2_assignedInvalidFoodCategory_validationExceptionIsThrown() throws Exception {
        food = foodService.saveFood(food);
        assertThrows(ValidationException.class, () -> {
            food = foodService.assignCategory(food.getId(), -1l);
        });
    }

    @Test
    public void F3_assignFoodToCategory_returnFoodCategories() throws Exception {
        food = foodService.saveFood(food);
        category = categoryService.saveCategory(category);
        food = foodService.assignCategory(food.getId(), category.getId());
        assertEquals(category, categoryService.getAllFoodCategories().get(0));
    }

    @Test
    public void improvisedTest_saveFoodWithMultipleCategories_returnSavedFood() throws Exception {
        // save categories
        category = categoryService.saveCategory(category);
        category2 = categoryService.saveCategory(category2);
        category3 = categoryService.saveCategory(category3);

        // add set of categories to food entity
        List<Category> catSet = new ArrayList<>();
        catSet.add(category);
        catSet.add(category2);
        catSet.add(category3);
        food.setCategories(catSet);

        // save food
        food = foodService.saveFood(food);

        // check, if persisted food has all categories
        catSet = food.getCategories();
        assertTrue(catSet.contains(category));
        assertTrue(catSet.contains(category2));
        assertTrue(catSet.contains(category3));

        // check if categories have food
        category = categoryService.findOne(category.getId());
        category2 = categoryService.findOne(category2.getId());
        category3 = categoryService.findOne(category3.getId());
        List<Food> foodList= category.getAssignedFood();
        assertTrue(foodList.contains(food));
        foodList= category2.getAssignedFood();
        assertTrue(foodList.contains(food));
        foodList= category3.getAssignedFood();
        assertTrue(foodList.contains(food));

        // get persisted food again
        Food gotFood = foodService.findOne(food.getId());

        // check if categories in food is not empty
        catSet = gotFood.getCategories();
        assertFalse(catSet.isEmpty());


        // update food
        gotFood.setName("new name");
        foodService.patch(gotFood);


        // check, if all categories are there
        assertTrue(catSet.size() == 3);
        for (Category savedCat: catSet) {
            if (savedCat.getName().equals(category2.getName())) {
                assertEquals(savedCat.getId(), category2.getId());
                assertEquals(savedCat.getImage(), category2.getImage());
                assertEquals(savedCat.getImageContentType(), category2.getImageContentType());
                assertEquals(savedCat.getDescription(), category2.getDescription());
                assertEquals(savedCat.getShow(), category2.getShow());
                assertFalse(savedCat.getAssignedFood().isEmpty());
                assertEquals(savedCat.getAssignedFood(), category2.getAssignedFood());
            }
        }
        assertTrue(catSet.contains(category));
        assertTrue(catSet.contains(category2));
        assertTrue(catSet.contains(category3));


        // delete food again
        catSet = gotFood.getCategories();
        for (Category cat: catSet){
            List<Food> l = cat.getAssignedFood();
            l.remove(gotFood);
            cat.setAssignedFood(l);
            categoryService.saveCategory(cat);
        }
        //gotFood.setCategories(new HashSet<>());
        //foodRepository.save(gotFood);
        foodRepository.delete(gotFood);

        //foodService.deleteFood(gotFood.getId());

        // check, if categories still have food saved

        category = categoryService.findOne(category.getId());
        category2 = categoryService.findOne(category2.getId());
        category3 = categoryService.findOne(category3.getId());
        foodList= category.getAssignedFood();
        assertFalse(foodList.contains(food));
        foodList= category2.getAssignedFood();
        assertFalse(foodList.contains(food));
        foodList= category3.getAssignedFood();
        assertFalse(foodList.contains(food));


    }


    @Test
    public void F4_assignedCategory_foodReturned() throws Exception {
        food = foodService.saveFood(food);
        category = categoryService.saveCategory(category);
        food = foodService.assignCategory(food.getId(), category.getId());
        assertFalse(food.getCategories().isEmpty());
        food = foodService.removeCategory(food.getId(), category.getId());

        categoryService.deleteCategory(category.getId());
        assertTrue(food.getCategories().isEmpty());
    }

    @Test
    public void F5_assignedCategory_drinkReturned() throws Exception {
        drink = drinkService.saveDrink(drink);
        category = categoryService.saveCategory(category);
        drink = drinkService.assignCategory(drink.getId(), category.getId());
        assertFalse(drink.getCategories().isEmpty());
    }

    @Test
    public void F6_assignedInvalidDrinkCategory_validationExceptionIsThrown() throws Exception {
        drink = drinkService.saveDrink(drink);
        assertThrows(ValidationException.class, () -> {
            drink = drinkService.assignCategory(drink.getId(), -1l);
        });
    }

    @Test
    public void F7_assignDrinkToCategory_returnDrinkCategories() throws Exception {
        drink = drinkService.saveDrink(drink);
        category = categoryService.saveCategory(category);
        drink = drinkService.assignCategory(drink.getId(), category.getId());
        assertEquals(category, categoryService.getAllDrinkCategories().get(0));
    }

    */

}
