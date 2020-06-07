package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CompleteFoodDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SpecificFoodMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.FoodService;
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
//@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
@RequestMapping(value = "/api/v1/foods")
public class FoodEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final FoodService foodService;
    private final SpecificFoodMapper foodMapper;

    @Autowired
    public FoodEndpoint(FoodService foodService, SpecificFoodMapper specificFoodMapper) {
        this.foodService = foodService;
        this.foodMapper = specificFoodMapper;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create a new food", authorizations = {@Authorization(value = "apiKey")})
    public CompleteFoodDto post(@Valid @RequestBody CompleteFoodDto foodDto) {
        LOGGER.info("POST /api/v1/food");
        try {
            return foodMapper.foodToCompleteFoodDto(foodService.saveFood(foodMapper.completeFoodDtoToFood(foodDto)));
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during creating food.");
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID already in use.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during creating food.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete the given food", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable Long id) {
        LOGGER.info("DELETE /api/v1/food by id: {}", id);
        try {
            foodService.deleteFood(id);
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during deleting food.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during deleting food.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during deleting food.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get food by ID", authorizations = {@Authorization(value = "apiKey")})
    public CompleteFoodDto find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/food by id: {}", id);
        try {
            return foodMapper.foodToCompleteFoodDto(foodService.findOne(id));
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid ID.", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting food.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting food.");
        }
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch the given food", authorizations = {@Authorization(value = "apiKey")})
    public CompleteFoodDto update(@Valid @RequestBody CompleteFoodDto completeFoodDto){
        LOGGER.info("PATCH /api/v1/food");
        try {
            return foodMapper.foodToCompleteFoodDto(foodService.patch(foodMapper.completeFoodDtoToFood(completeFoodDto)));
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during updating food.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during updating food.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during updating food.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiOperation(value = "Get list of foods", authorizations = {@Authorization(value = "apiKey")})
    public List<CompleteFoodDto> findAll() {
        LOGGER.info("GET /api/v1/foods");
        try {
            return foodMapper.foodToCompleteFoodDto(foodService.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every food.");
        }
    }
    
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/foodsByName")
    @ApiOperation(value = "Get list of foods by name", authorizations = {@Authorization(value = "apiKey")})
    public List<CompleteFoodDto> findFoodsByName(@RequestParam String name) {
        LOGGER.info("GET /api/v1/foods");
        try {
            return foodMapper.foodToCompleteFoodDto(foodService.findFoodsByName(name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every food.");
        }
    }


    @GetMapping(value = "deleted")
    @ApiOperation(value = "Get list of foods", authorizations = {@Authorization(value = "apiKey")})
    public List<CompleteFoodDto> getAllWithDeleted() {
        LOGGER.info("GET deleted /api/v1/foods");
        try {
            return foodMapper.foodToCompleteFoodDto(foodService.getAllWithDeleted());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every deleted food.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "updated")
    @ApiOperation(value = "Get list of foods", authorizations = {@Authorization(value = "apiKey")})
    public List<CompleteFoodDto> getAllWithUpdated() {
        LOGGER.info("GET updated /api/v1/foods");
        try {
            return foodMapper.foodToCompleteFoodDto(foodService.getAllWithUpdated());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every updated food.");
        }
    }

}
