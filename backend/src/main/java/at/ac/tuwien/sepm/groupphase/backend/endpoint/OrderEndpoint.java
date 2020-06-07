package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PayDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleFoodDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleOrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SpecificOrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.DrinkService;
import at.ac.tuwien.sepm.groupphase.backend.service.FoodService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderService orderService;
    private final SpecificOrderMapper specificOrderMapper;

    private final FoodService foodService;
    private final UserService userService;
    private final DrinkService drinkService;

    @Autowired
    public OrderEndpoint(OrderService orderService, SpecificOrderMapper specificOrderMapper, FoodService foodService, UserService userService, DrinkService drinkService) {
        this.orderService = orderService;
        this.specificOrderMapper = specificOrderMapper;
        this.foodService = foodService;
        this.userService = userService;
        this.drinkService = drinkService;
    }

    //TODO delete this method only for testing made by martin
    @PostMapping(path = "/test")
    public void test() {


        Order orderPatch = Order.OrderBuilder.aOrder()
            .withId(3L)
            .withStatus(Status.NEU)
            .withAssistance("schnelll")
            .withStartDate(null)
            .withEndDate(null)
            .withTable(userService.findOne(1L))
            .withFoods(foodService.findAll())
            .withDrinks(drinkService.findAll())
            .build();
        orderService.createOrder(orderPatch);
    }


    //@Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create a new order", authorizations = {@Authorization(value = "apiKey")})
    public SimpleOrderDto post(@Valid @RequestBody OrderDto orderDto) {
        LOGGER.info("POST /api/v1/orders body: {}", orderDto);
        try {
            return specificOrderMapper.OrderToSimpleOrderDto(orderService.createOrder(specificOrderMapper.OrderDtoToOrder(orderDto)));
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during creating order.");
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID already in use.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during creating order.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ApiOperation(value = "Get list of orders", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleOrderDto> findAll() {
        LOGGER.info("GET /api/v1/orders");
        try {
            List<SimpleOrderDto> returnList = new ArrayList<>();

            for (Order o : orderService.findAll()) {
                returnList.add(specificOrderMapper.OrderToSimpleOrderDto(o));
            }
            return returnList;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting all orders.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting all orders.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/pay")
    @ApiOperation(value = "Get list of not finished orders grouped by tables", authorizations = {@Authorization(value = "apiKey")})
    public List<PayDto> findNotErledigtGroupByTable() {
        LOGGER.info("GET /api/v1/orders/group");
        try {
            return specificOrderMapper.OrdersToPayDto(orderService.findAll());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting all orders for paying.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting all orders for paying.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/new")
    @ApiOperation(value = "Get list of orders with status new", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleOrderDto> findNew() {
        LOGGER.info("GET /api/v1/orders/new");
        try {
            List<SimpleOrderDto> returnList = new ArrayList<>();

            for (Order o : orderService.findNew()) {
                returnList.add(specificOrderMapper.OrderToSimpleOrderDto(o));
            }
            return returnList;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting all orders.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting all orders.");
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/serve")
    @ApiOperation(value = "Get list of orders with status serve", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleOrderDto> findServe() {
        LOGGER.info("GET /api/v1/orders/serve");
        try {
            List<SimpleOrderDto> returnList = new ArrayList<>();

            for (Order o : orderService.findServe()) {
                returnList.add(specificOrderMapper.OrderToSimpleOrderDto(o));
            }
            return returnList;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during getting all orders.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during getting all orders.");
        }
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch the status", authorizations = {@Authorization(value = "apiKey")})
    public SimpleOrderDto patch(@RequestParam(value = "id") Long id,
                                @RequestParam(value = "status")String status){
        LOGGER.info("PATCH /api/v1/orders id {} to status {}", id, status);
        try {
            return specificOrderMapper.OrderToSimpleOrderDto(orderService.setStatus(id, status));
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during updating order.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during updating order.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during updating order.");
        }
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping(path = "/pay")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch the status", authorizations = {@Authorization(value = "apiKey")})
    public List<SimpleOrderDto> patch(@RequestParam(value = "tableId") Long tableId){
        LOGGER.info("PATCH /api/v1/orders tableId {} to status ERLEDIGT", tableId);
        try {
            List<SimpleOrderDto> returnList = new ArrayList<>();

            for (Order o : orderService.setStatusPerTable(tableId)) {
                returnList.add(specificOrderMapper.OrderToSimpleOrderDto(o));
            }
            return returnList;
        } catch (ConstraintViolationException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error during updating order.");
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during updating order.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during updating order.");
        }
    }
}
