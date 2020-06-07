package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImagelessCategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CategoryMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryEndpoint(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create a new category", authorizations = {@Authorization(value = "apiKey")})
    public CategoryDto post(@Valid @RequestBody CategoryDto categoryDto) {
        LOGGER.info("POST /api/v1/category");
        try {
            return categoryMapper.categoryToCategoryDto(categoryService.saveCategory(categoryMapper.categoryDtoToCategoryEntity(categoryDto)));
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during creating category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during creating category.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete the given category", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable Long id) {
        LOGGER.info("DELETE /api/v1/category by id: {}", id);
        try {
            categoryService.deleteCategory(id);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during deleting category.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during deleting category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during deleting category.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get category by ID", authorizations = {@Authorization(value = "apiKey")})
    public CategoryDto find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/category by id: {}", id);
        try {
            return categoryMapper.categoryToCategoryDto(categoryService.findOne(id));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID.", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting category.");
        }
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch the given category", authorizations = {@Authorization(value = "apiKey")})
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto){
        LOGGER.info("PATCH /api/v1/category");
        try {
            return categoryMapper.categoryToCategoryDto(categoryService.patch(categoryMapper.categoryDtoToCategoryEntity(categoryDto)));
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during updating category.");
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID.", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during updating category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during updating category.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiOperation(value = "Get list of foods", authorizations = {@Authorization(value = "apiKey")})
    public List<CategoryDto> findAll() {
        LOGGER.info("GET /api/v1/category");
        try {
            return categoryMapper.categoryToCategoryDto(categoryService.findAll());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting every category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every category.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/food")
    @ApiOperation(value = "Get list of food categories", authorizations = {@Authorization(value = "apiKey")})
    public List<CategoryDto> findAllFoodCategories() {
        LOGGER.info("GET /api/v1/category/food");
        try {
            return categoryMapper.categoryToCategoryDto(categoryService.getAllFoodCategories());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting every food category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every food category.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/drink")
    @ApiOperation(value = "Get list of drink categories", authorizations = {@Authorization(value = "apiKey")})
    public List<CategoryDto> findAllDrinkCategories() {
        LOGGER.info("GET /api/v1/category/drink");
        try {
            return categoryMapper.categoryToCategoryDto(categoryService.getAllDrinkCategories());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting every drink category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every drink category.");
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/food/{id}")
    @ApiOperation(value = "Get all categories assigned to food with id that have show-flag", authorizations = {@Authorization(value = "apiKey")})
    public List<ImagelessCategoryDto> findAllAssignedToFood(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/category/food by id: {}", id);
        try {
            return categoryMapper.categoryToImagelessCategoryDto(categoryService.findAssignedToFood(id));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID.", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting category.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/drink/{id}")
    @ApiOperation(value = "Get all categories assigned to drink with id that have show-flag", authorizations = {@Authorization(value = "apiKey")})
    public List<ImagelessCategoryDto> findAllAssignedToDrink(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/category/drink by id: {}", id);
        try {
            return categoryMapper.categoryToImagelessCategoryDto(categoryService.findAssignedToDrink(id));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID.", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting category.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting category.");
        }
    }

}
