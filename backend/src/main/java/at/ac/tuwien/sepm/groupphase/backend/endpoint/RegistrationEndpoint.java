package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleOrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TableRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TableRegistrationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RestController
@RequestMapping(value = "/api/v1/registration")
@CrossOrigin
public class RegistrationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
   private  UserService userService;
   private TableRegistrationMapper tableRegistrationMapper;
    @Autowired
    public RegistrationEndpoint(UserService userService, TableRegistrationMapper tableRegistrationMapper) {
        this.userService = userService;
        this.tableRegistrationMapper = tableRegistrationMapper;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Register user")
    public void register(@RequestBody UserRegistrationDto registrationDto ){
        LOGGER.info("POST /api/v1/registration");

       // return messageMapper.messageToSimpleMessageDto(messageService.findAll());

        ApplicationUser test = new ApplicationUser();

        test.setEmail(registrationDto.getEmail());
        test.setName(registrationDto.getName());
        test.setPassword(registrationDto.getPassword());
        test.setAdmintype("Admin");

        ApplicationUser created = userService.createApplicationUser(test);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/tables")
    @ApiOperation(value = "Register Table")
    public void registerTable(@RequestBody TableRegistrationDto tableRegistrationDto ){
        LOGGER.info("POST /api/v1/registration/");

        ApplicationUser created = userService.createApplicationUser(tableRegistrationMapper
            .tableRegistrationDtoToApplicationUser(tableRegistrationDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiOperation(value = "Get list of all users", authorizations = {@Authorization(value = "apiKey")})
    public List<TableRegistrationDto> findAll() {
        LOGGER.info("GET /api/v1/registration/");
        try {
            List<TableRegistrationDto> returnList = new ArrayList<>();

            for (ApplicationUser o : userService.findAll()) {
                returnList.add(tableRegistrationMapper.ApplicationUserToTableRegistrationDto(o));
            }
            return returnList;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting orders without status ERLEDIGT.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting orders without status ERLEDIGT.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{name}")
    @ApiOperation(value = "Delete the given table if not in use", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable String name) {
        LOGGER.info("DELETE /api/v1/registration by name: {}", name);
        try {
           userService.deleteTable(name);
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during deleting table.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during deleting table.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during deleting table." + e);
        }
    }


}
