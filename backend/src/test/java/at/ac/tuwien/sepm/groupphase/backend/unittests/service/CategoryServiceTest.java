package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.CategoryService;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    private Category category;

    @BeforeEach
    public void beforeEach() {
        category = Category.CategoryBuilder.aCategory()
            .withId(1L)
            .withName("TestCategory")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();
    }

    @AfterEach
    public void afterEach() {
        try {
            List<Category> categories = categoryService.findAll();
            for (Category category : categories) {
                Long id = category.getId();
                categoryService.deleteCategory(id);
            }
        } catch (NotFoundException ignored) {
        }
    }

    @Test
    public void F1_createCategory_categoryIsValid_categoryWillBePersisted() throws Exception {
        category = Category.CategoryBuilder.aCategory()
            .withId(null)
            .withName("TestCategory")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        Category createdCategory = categoryService.saveCategory(category);
        category.setId(createdCategory.getId());
        assertEquals(category, createdCategory);
    }

    @Test
    public void F2_createFood_foodIsValid_foodWillBePersisted() throws Exception {
        String oneHundred = "X".repeat(100);
        String tenThousand = "XXXXXXXXXX".repeat(1000);

        category = Category.CategoryBuilder.aCategory()
            .withId(100L)
            .withName(oneHundred)
            .withDescription(tenThousand)
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        assertEquals(category, categoryService.saveCategory(category));
    }

    @Test
    public void F3_createFood_foodIsValid_foodWillBePersisted() throws Exception {
        category = Category.CategoryBuilder.aCategory()
            .withId(Long.MAX_VALUE)
            .withName("TestCategory")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        assertEquals(category, categoryService.saveCategory(category));
    }

    @Test
    public void F4_createFood_imageNotBase64_validationExceptionIsThrown() throws Exception {
        category.setImage("Not base64");
        assertThrows(ValidationException.class, () -> {
            categoryService.saveCategory(category);
        });
    }

    @Test
    public void F5_createFood_imageTypeNotImage_validationExceptionIsThrown() throws Exception {
        category.setImageContentType("not image type");
        assertThrows(ValidationException.class, () -> {
            categoryService.saveCategory(category);
        });
    }

    @Test
    public void F6_deleteFood_idExistsAlready_foodIsDeleted() throws Exception {
        Category newCategory = categoryService.saveCategory(category);
        categoryService.deleteCategory(newCategory.getId());
        assertThrows(NotFoundException.class, () -> {
            categoryService.deleteCategory(newCategory.getId());
        });
    }

    @Test
    public void F7_deleteFood_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        assertThrows(NotFoundException.class, () -> {
            categoryService.deleteCategory(100L);
        });
    }

    @Test
    public void F8_patch_validUpdate_entityIsUpdated() throws Exception {
        category = categoryService.saveCategory(category);
        Category categoryPatch = Category.CategoryBuilder.aCategory()
            .withId(category.getId())
            .withName("TestCategory")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        assertEquals(categoryPatch, categoryService.patch(categoryPatch));
    }

    @Test
    public void F9_patch_validUpdate_entityIsUpdated() throws Exception {
        category = categoryService.saveCategory(category);
        String oneHundred = "X".repeat(100);
        String tenThousand = "XXXXXXXXXX".repeat(1000);
        Category categoryPatch = Category.CategoryBuilder.aCategory()
            .withId(category.getId())
            .withName(oneHundred)
            .withDescription(tenThousand)
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        assertEquals(categoryPatch, categoryService.patch(categoryPatch));
    }

    @Test
    public void F10_patch_validUpdate_entityIsUpdated() throws Exception {
        category = categoryService.saveCategory(category);
        Category categoryPatch = Category.CategoryBuilder.aCategory()
            .withId(category.getId())
            .withName("Name")
            .withDescription("Description")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();
        assertEquals(categoryPatch, categoryService.patch(categoryPatch));
    }

    @Test
    public void F11_patch_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        category.setId(100L);
        assertThrows(NotFoundException.class, () -> {
            categoryService.patch(category);
        });
    }

    @Test
    public void F12_patch_imageIsNotBase64_ValidationExceptionIsThrown() throws Exception {
        category.setImage("Not Base64");
        assertThrows(ValidationException.class, () -> {
            categoryService.patch(category);
        });
    }

    @Test
    public void F13_patch_imageContentTypeisInvalid_ValidationExceptionIsThrown() throws Exception {
        category.setImageContentType("no valid image type");
        assertThrows(ValidationException.class, () -> {
            categoryService.patch(category);
        });
    }

    @Test
    public void F14_findOne_idExistsAlready_entityIsReturned() throws Exception {
        Category savedCategory = categoryService.saveCategory(category);
        assertEquals(savedCategory.toString(), categoryService.findOne(savedCategory.getId()).toString());
    }

    @Test
    public void F15_findOne_invalidNonNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            categoryService.findOne(0L);
        });
    }

    @Test
    public void F16_findOne_invalidNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            categoryService.findOne(-1L);
        });
    }

    @Test
    public void F17_findOne_idDoesntExistsAlready_NotFoundExceptionIsThrown() throws Exception {
        assertThrows(NotFoundException.class, () -> {
            categoryService.findOne(200L);
        });
    }

    @Test
    public void F18_findAll_OnePersistedFood_AllFoodsAreReturned() throws Exception {
        Category savedCategory = categoryService.saveCategory(category);
        assertEquals(savedCategory.toString(), categoryService.findAll().get(0).toString());
    }

    @Test
    public void F19_findAll_ThreePersistedFoods_AllFoodsAreReturned() throws Exception {
        Category savedCategory1 = categoryService.saveCategory(category);
        Category savedCategory2 = categoryService.saveCategory(category);
        Category savedCategory3 = categoryService.saveCategory(category);

        List<Category> categories = categoryService.findAll();
        assertTrue(categories.size() == 3);
        //assertTrue(categories.contains(savedCategory1));
        //assertTrue(categories.contains(savedCategory2));
        //assertTrue(categories.contains(savedCategory3));
    }

    @Test
    public void F20_findAll_NoPersistedFood_NotFoundExceptionIsThrown() throws Exception {
        List<Category> categories = categoryService.findAll();
        assertTrue(categories.isEmpty());
    }


}

