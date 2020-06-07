package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DrinkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SpecificDrinkMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.DrinkService;
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
@RequestMapping(value = "/api/v1/drinks")
public class DrinkEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DrinkService drinkService;
    private final SpecificDrinkMapper drinkMapper;

    @Autowired
    public DrinkEndpoint(DrinkService drinkService, SpecificDrinkMapper drinkMapper) {
        this.drinkService = drinkService;
        this.drinkMapper = drinkMapper;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create a new drink", authorizations = {@Authorization(value = "apiKey")})
    public DrinkDto post(@Valid @RequestBody DrinkDto drinkDto) {
        LOGGER.info("POST /api/v1/drinks");
        try {
            return drinkMapper.drinkToDrinkDto(drinkService.saveDrink(drinkMapper.drinkDtoToDrink(drinkDto)));
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during creating drink.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during creating drink.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete the given drink", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable Long id) {
        LOGGER.info("DELETE /api/v1/drink by id: {}", id);
        try {
            drinkService.deleteDrink(id);
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during deleting drind.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during deleting drind.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during deleting drind.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get drink by ID", authorizations = {@Authorization(value = "apiKey")})
    public DrinkDto find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/drink by id: {}", id);
        try {
            return drinkMapper.drinkToDrinkDto(drinkService.findOne(id));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid ID.", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting drink.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting drink.");
        }
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch the given drink", authorizations = {@Authorization(value = "apiKey")})
    public DrinkDto update(@Valid @RequestBody DrinkDto drinkDto){
        LOGGER.info("PATCH /api/v1/drink");
        try {
            return drinkMapper.drinkToDrinkDto(drinkService.patch(drinkMapper.drinkDtoToDrink(drinkDto)));
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during updating drink.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during updating drink.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during updating drink.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiOperation(value = "Get list of drinks", authorizations = {@Authorization(value = "apiKey")})
    public List<DrinkDto> findAll() {
        LOGGER.info("GET /api/v1/drinks");
        try {
            return drinkMapper.drinkToDrinkDto(drinkService.findAll());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every drink.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/drinksByName")
    @ApiOperation(value = "Get list of drinks by name", authorizations = {@Authorization(value = "apiKey")})
    public List<DrinkDto> findDrinksByName(@RequestParam String name) {
        LOGGER.info("GET /api/v1/drinks");
        try {
            return drinkMapper.drinkToDrinkDto(drinkService.findDrinksByName(name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every drink.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "deleted")
    @ApiOperation(value = "Get list of drinks", authorizations = {@Authorization(value = "apiKey")})
    public List<DrinkDto> getAllWithDeleted() {
        LOGGER.info("GET deleted /api/v1/drinks");
        try {
            return drinkMapper.drinkToDrinkDto(drinkService.getAllWithDeleted());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every deleted drink.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "updated")
    @ApiOperation(value = "Get list of drinks", authorizations = {@Authorization(value = "apiKey")})
    public List<DrinkDto> getAllWithUpdated() {
        LOGGER.info("GET updated /api/v1/drinks");
        try {
            return drinkMapper.drinkToDrinkDto(drinkService.getAllWithUpdated());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting every deleted drink.");
        }
    }
}
