package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.DrinkService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DrinkServiceTest {

    @Autowired
    private DrinkService drinkService;
    private Drink drink;

    @BeforeEach
    public void beforeEach() {
        drink = Drink.DrinkBuilder.aDrink()
            .withId(1L)
            .withName("Testname")
            .withDescription("Desc")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .withAssignedCategories(new ArrayList<>())
            .withAlcohol(5)
            .withDeleted(false)
            .withUpdated(false)
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
        } catch (NotFoundException ignored) {
        }
    }

    @Test
    public void F1_createDrink_drinkIsValid_drinkWillBePersisted() throws Exception {
        drink = Drink.DrinkBuilder.aDrink()
            .withId(null)
            .withName("TestDrink")
            .withDescription("")
            .withPrice(new BigDecimal("0.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withAlcohol(0)
            .withAssignedCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        Drink createdDrink = drinkService.saveDrink(drink);
        drink.setId(createdDrink.getId());
        assertEquals(drink, createdDrink);
    }

    @Test
    public void F2_createDrink_drinkIsValid_drinkWillBePersisted() throws Exception {
        String oneHundred = "X".repeat(100);
        String tenThousand = "XXXXXXXXXX".repeat(1000);

        drink = Drink.DrinkBuilder.aDrink()
            .withId(100L)
            .withName(oneHundred)
            .withDescription(tenThousand)
            .withPrice(new BigDecimal("1000.45"))
            .withImage("ABBKRklGAAEBAQBIAEgAAAATQ3JlYXRlZCB3aXRoIEdJTVAAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDRcYFhQYEhQVFABDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQAEQgAAQABAwERAAIRAQMRAQAUAAEAAAAAAAAAAAAAAAAAAAAIABQBAQAAAAAAAAAAAAAAAAAAAAAADAMBAAIQAxAAAAFUABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAEFAn8AFBEBAAAAAAAAAAAAAAAAAAAAAAAIAQMBAT8BfwAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAgEBPwF/ABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAY/An8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8hfwAMAwEAAgADAAAAEAAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAwEBPxB/ABQRAQAAAAAAAAAAAAAAAAAAAAAACAECAQE/EH8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8Qfw==")
            .withImageContentType("image/jpeg")
            .withAlcohol(Integer.MAX_VALUE)
            .withAssignedCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(drink, drinkService.saveDrink(drink));
    }

    @Test
    public void F3_createDrink_drinkIsValid_drinkWillBePersisted() throws Exception {
        drink = Drink.DrinkBuilder.aDrink()
            .withId(Long.MAX_VALUE)
            .withName("Name")
            .withDescription("Description")
            .withPrice(new BigDecimal("9934.99"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .withAlcohol(5)
            .withAssignedCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(drink, drinkService.saveDrink(drink));
    }

    @Test
    public void F4_createDrink_imageNotBase64_validationExceptionIsThrown() throws Exception {
        drink.setImage("Not base64");
        assertThrows(ValidationException.class, () -> {
            drinkService.saveDrink(drink);
        });
    }

    @Test
    public void F5_createDrink_imageTypeNotImage_validationExceptionIsThrown() throws Exception {
        drink.setImageContentType("not image type");
        assertThrows(ValidationException.class, () -> {
            drinkService.saveDrink(drink);
        });
    }

    @Test
    public void F6_deleteDrink_idExistsAlready_drinkIsDeleted() throws Exception {
        Drink newdrink = drinkService.saveDrink(drink);
        drinkService.deleteDrink(newdrink.getId());
        assertThrows(NotFoundException.class, () -> {
            drinkService.deleteDrink(newdrink.getId());
        });
    }

    @Test
    public void F7_deleteDrink_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        assertThrows(NotFoundException.class, () -> {
            drinkService.deleteDrink(100L);
        });
    }

    @Test
    public void F8_patch_validUpdate_entityIsUpdated() throws Exception {
        drink = drinkService.saveDrink(drink);
        Drink drinkPatch = Drink.DrinkBuilder.aDrink()
            .withId(drink.getId())
            .withName("Testdrink")
            .withDescription("")
            .withPrice(new BigDecimal("0.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withAlcohol(0)
            .withAssignedCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(drinkPatch, drinkService.patch(drinkPatch));
    }

    @Test
    public void F9_patch_validUpdate_entityIsUpdated() throws Exception {
        drink = drinkService.saveDrink(drink);
        String oneHundred = "X".repeat(100);
        String tenThousand = "XXXXXXXXXX".repeat(1000);
        Drink drinkPatch = Drink.DrinkBuilder.aDrink()
            .withId(drink.getId())
            .withName(oneHundred)
            .withDescription(tenThousand)
            .withPrice(new BigDecimal("1000.45"))
            .withImage("ABBKRklGAAEBAQBIAEgAAAATQ3JlYXRlZCB3aXRoIEdJTVAAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDRcYFhQYEhQVFABDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQAEQgAAQABAwERAAIRAQMRAQAUAAEAAAAAAAAAAAAAAAAAAAAIABQBAQAAAAAAAAAAAAAAAAAAAAAADAMBAAIQAxAAAAFUABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAEFAn8AFBEBAAAAAAAAAAAAAAAAAAAAAAAIAQMBAT8BfwAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAgEBPwF/ABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAY/An8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8hfwAMAwEAAgADAAAAEAAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAwEBPxB/ABQRAQAAAAAAAAAAAAAAAAAAAAAACAECAQE/EH8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8Qfw==")
            .withImageContentType("image/jpeg")
            .withAlcohol(Integer.MAX_VALUE)
            .withAssignedCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(drinkPatch, drinkService.patch(drinkPatch));
    }

    @Test
    public void F10_patch_validUpdate_entityIsUpdated() throws Exception {
        drink = drinkService.saveDrink(drink);
        Drink drinkPatch = Drink.DrinkBuilder.aDrink()
            .withId(drink.getId())
            .withName("Name")
            .withDescription("Description")
            .withPrice(new BigDecimal("9934.99"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA")
            .withImageContentType("image/png")
            .withAlcohol(5)
            .withAssignedCategories(new ArrayList<>())
            .withDeleted(false)
            .withUpdated(false)
            .build();

        assertEquals(drinkPatch, drinkService.patch(drinkPatch));
    }

    @Test
    public void F11_patch_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        drink.setId(100L);
        assertThrows(NotFoundException.class, () -> {
            drinkService.patch(drink);
        });
    }

    @Test
    public void F12_patch_imageIsNotBase64_ValidationExceptionIsThrown() throws Exception {
        drink.setImage("Not Base64");
        assertThrows(ValidationException.class, () -> {
            drinkService.patch(drink);
        });
    }

    @Test
    public void F13_patch_imageContentTypeisInvalid_ValidationExceptionIsThrown() throws Exception {
        drink.setImageContentType("no valid image type");
        assertThrows(ValidationException.class, () -> {
            drinkService.patch(drink);
        });
    }

    @Test
    public void F14_findOne_idExistsAlready_entityIsReturned() throws Exception {
        Drink savedDrink = drinkService.saveDrink(drink);
        assertEquals(savedDrink, drinkService.findOne(savedDrink.getId()));
    }

    @Test
    public void F15_findOne_invalidNonNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            drinkService.findOne(0L);
        });
    }

    @Test
    public void F16_findOne_invalidNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            drinkService.findOne(-1L);
        });
    }

    @Test
    public void F17_findOne_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        assertThrows(NotFoundException.class, () -> {
            drinkService.findOne(200L);
        });
    }

    @Test
    public void F18_findAll_OnePersistedDrink_AllDrinksAreReturned() throws Exception {
        Drink savedDrink = drinkService.saveDrink(drink);
        assertEquals(savedDrink, drinkService.findAll().get(0));
    }

    @Test
    public void F19_findAll_ThreePersistedDrinks_AllDrinksAreReturned() throws Exception {
        Drink savedDrink1 = drinkService.saveDrink(drink);
        Drink savedDrink2 = drinkService.saveDrink(drink);
        Drink savedDrink3 = drinkService.saveDrink(drink);

        List<Drink> drinks = drinkService.findAll();
        assertTrue(drinks.contains(savedDrink1));
        assertTrue(drinks.contains(savedDrink2));
        assertTrue(drinks.contains(savedDrink3));
    }

    @Test
    public void F20_findAll_NoPersistedDrink_emptyListReturned() throws Exception {
        List<Drink> drinks = drinkService.findAll();
        assertTrue(drinks.isEmpty());
    }

}
