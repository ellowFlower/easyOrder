package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.FoodService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FoodServiceTest {

    @Autowired
    private FoodService foodService;

    private Food food;

    @BeforeEach
    public void beforeEach() {
        food = Food.FoodBuilder.aFood()
            .withId(1L)
            .withName("Testname")
            .withDescription("Desc")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .withCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();
    }

    @AfterEach
    public void afterEach() {
        try {
            List<Food> foods = foodService.findAll();
            for (Food food : foods) {
                Long id = food.getId();
                foodService.deleteFood(id);
            }
        } catch (NotFoundException ignored) {
        }
    }

    @Test
    public void F1_createFood_foodIsValid_foodWillBePersisted() throws Exception {
        food = Food.FoodBuilder.aFood()
            .withId(null)
            .withName("TestFood")
            .withDescription("")
            .withPrice(new BigDecimal("0.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        Food createdFood = foodService.saveFood(food);
        food.setId(createdFood.getId());
        assertEquals(food, createdFood);
    }

    @Test
    public void F2_createFood_foodIsValid_foodWillBePersisted() throws Exception {
        String oneHundred = "X".repeat(100);
        String tenThousand = "XXXXXXXXXX".repeat(1000);

        food = Food.FoodBuilder.aFood()
            .withId(100L)
            .withName(oneHundred)
            .withDescription(tenThousand)
            .withPrice(new BigDecimal("1000.45"))
            .withImage("ABBKRklGAAEBAQBIAEgAAAATQ3JlYXRlZCB3aXRoIEdJTVAAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDRcYFhQYEhQVFABDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQAEQgAAQABAwERAAIRAQMRAQAUAAEAAAAAAAAAAAAAAAAAAAAIABQBAQAAAAAAAAAAAAAAAAAAAAAADAMBAAIQAxAAAAFUABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAEFAn8AFBEBAAAAAAAAAAAAAAAAAAAAAAAIAQMBAT8BfwAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAgEBPwF/ABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAY/An8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8hfwAMAwEAAgADAAAAEAAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAwEBPxB/ABQRAQAAAAAAAAAAAAAAAAAAAAAACAECAQE/EH8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8Qfw==")
            .withImageContentType("image/jpeg")
            .withCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(food, foodService.saveFood(food));
    }

    @Test
    public void F3_createFood_foodIsValid_foodWillBePersisted() throws Exception {
        food = Food.FoodBuilder.aFood()
            .withId(Long.MAX_VALUE)
            .withName("Name")
            .withDescription("Description")
            .withPrice(new BigDecimal("9934.99"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .withCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(food, foodService.saveFood(food));
    }

    @Test
    public void F4_createFood_imageNotBase64_validationExceptionIsThrown() throws Exception {
        food.setImage("Not base64");
        assertThrows(ValidationException.class, () -> {
            foodService.saveFood(food);
        });
    }

    @Test
    public void F5_createFood_imageTypeNotImage_validationExceptionIsThrown() throws Exception {
        food.setImageContentType("not image type");
        assertThrows(ValidationException.class, () -> {
            foodService.saveFood(food);
        });
    }

    @Test
    public void F6_deleteFood_idExistsAlready_foodIsDeleted() throws Exception {
        Food newfood = foodService.saveFood(food);
        foodService.deleteFood(newfood.getId());
        assertThrows(NotFoundException.class, () -> {
            foodService.deleteFood(newfood.getId());
        });
    }

    @Test
    public void F7_deleteFood_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        assertThrows(NotFoundException.class, () -> {
            foodService.deleteFood(100L);
        });
    }

    @Test
    public void F8_patch_validUpdate_entityIsUpdated() throws Exception {
        food = foodService.saveFood(food);
        Food foodPatch = Food.FoodBuilder.aFood()
            .withId(food.getId())
            .withName("TestFood")
            .withDescription("")
            .withPrice(new BigDecimal("0.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(foodPatch, foodService.patch(foodPatch));
    }

    @Test
    public void F9_patch_validUpdate_entityIsUpdated() throws Exception {
        food = foodService.saveFood(food);
        String oneHundred = "X".repeat(100);
        String tenThousand = "XXXXXXXXXX".repeat(1000);
        Food foodPatch = Food.FoodBuilder.aFood()
            .withId(food.getId())
            .withName(oneHundred)
            .withDescription(tenThousand)
            .withPrice(new BigDecimal("1000.45"))
            .withImage("ABBKRklGAAEBAQBIAEgAAAATQ3JlYXRlZCB3aXRoIEdJTVAAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDRcYFhQYEhQVFABDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQAEQgAAQABAwERAAIRAQMRAQAUAAEAAAAAAAAAAAAAAAAAAAAIABQBAQAAAAAAAAAAAAAAAAAAAAAADAMBAAIQAxAAAAFUABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAEFAn8AFBEBAAAAAAAAAAAAAAAAAAAAAAAIAQMBAT8BfwAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAgEBPwF/ABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAY/An8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8hfwAMAwEAAgADAAAAEAAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAwEBPxB/ABQRAQAAAAAAAAAAAAAAAAAAAAAACAECAQE/EH8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8Qfw==")
            .withImageContentType("image/jpeg")
            .withCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(foodPatch, foodService.patch(foodPatch));
    }

    @Test
    public void F10_patch_validUpdate_entityIsUpdated() throws Exception {
        food = foodService.saveFood(food);
        Food foodPatch = Food.FoodBuilder.aFood()
            .withId(food.getId())
            .withName("Name")
            .withDescription("Description")
            .withPrice(new BigDecimal("9934.99"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .withCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(foodPatch, foodService.patch(foodPatch));
    }

    @Test
    public void F11_patch_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        food.setId(100L);
        assertThrows(NotFoundException.class, () -> {
            foodService.patch(food);
        });
    }

    @Test
    public void F12_patch_imageIsNotBase64_ValidationExceptionIsThrown() throws Exception {
        food.setImage("Not Base64");
        assertThrows(ValidationException.class, () -> {
            foodService.patch(food);
        });
    }

    @Test
    public void F13_patch_imageContentTypeisInvalid_ValidationExceptionIsThrown() throws Exception {
        food.setImageContentType("no valid image type");
        assertThrows(ValidationException.class, () -> {
            foodService.patch(food);
        });
    }

    @Test
    public void F14_findOne_idExistsAlready_entityIsReturned() throws Exception {
        Food savedFood = foodService.saveFood(food);
        assertEquals(savedFood, foodService.findOne(savedFood.getId()));
    }

    @Test
    public void F15_findOne_invalidNonNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            foodService.findOne(0L);
        });
    }

    @Test
    public void F16_findOne_invalidNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            foodService.findOne(-1L);
        });
    }

    @Test
    public void F17_findOne_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        assertThrows(NotFoundException.class, () -> {
            foodService.findOne(200L);
        });
    }

    @Test
    public void F18_findAll_OnePersistedFood_AllFoodsAreReturned() throws Exception {
        Food savedFood = foodService.saveFood(food);
        assertEquals(savedFood, foodService.findAll().get(0));
    }

    @Test
    public void F19_findAll_ThreePersistedFoods_AllFoodsAreReturned() throws Exception {
        Food savedFood1 = foodService.saveFood(food);
        Food savedFood2 = foodService.saveFood(food);
        Food savedFood3 = foodService.saveFood(food);

        List<Food> foods = foodService.findAll();
        assertTrue(foods.contains(savedFood1));
        assertTrue(foods.contains(savedFood2));
        assertTrue(foods.contains(savedFood3));
    }

    @Test
    public void F20_findAll_NoPersistedFood_NotFoundExceptionIsThrown() throws Exception {
        List<Food> foods = foodService.findAll();
        assertTrue(foods.isEmpty());
    }

}

