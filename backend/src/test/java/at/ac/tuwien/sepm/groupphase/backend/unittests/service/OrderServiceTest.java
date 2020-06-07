package at.ac.tuwien.sepm.groupphase.backend.unittests.service;


import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DrinkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FoodRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DrinkService;
import at.ac.tuwien.sepm.groupphase.backend.service.FoodService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private DrinkRepository drinkRepository;


    private Food food1;
    private Food food2;

    private Drink drink1;
    private Drink drink2;

    private List<Food> listOfFoods = new ArrayList<>();
    private List<Drink> listOfDrinks = new ArrayList<>();

    private Order order1;
    private Order order2;


    @BeforeEach
    public void beforeEach() {

        order1 = Order.OrderBuilder.aOrder()
            .withId(null)
            .withStatus(Status.NEU)
            .withAssistance(null)
            .withStartDate(null)
            .withEndDate(null)
            .build();

        order2 = Order.OrderBuilder.aOrder()
            .withId(null)
            .withStatus(Status.NEU)
            .withAssistance(null)
            .withStartDate(null)
            .withEndDate(null)
            .build();

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @AfterEach
    public void afterEach() {

        try {
            List<Order> orders = orderService.findAll();
            for (Order order : orders) {
                Long id = order.getId();
                orderService.deleteOrder(id);
            }
        } catch (NotFoundException ignored) {
        }
    }

    @Test
    public void F1_createOrder_OrderIsValid_OrderWillBePersisted() throws Exception {

        Order createdOrder = orderService.createOrder(order1);
        assertEquals(order1.getStatus(), createdOrder.getStatus());
        assertEquals(order1.getEndDate(), createdOrder.getEndDate());
        assertNotNull(createdOrder);

    }

    @Test
    public void F2_createOrder_OrderIsValid_OrderWillBePersisted() throws Exception {

        order1 = Order.OrderBuilder.aOrder()
            .withId(3L)
            .withStatus(Status.ERLEDIGT)
            .withAssistance("assistance")
            .withStartDate(null)
            .withEndDate(null)
            .build();

        Order createdOrder = orderService.createOrder(order1);
        assertEquals(order1.getStatus(), createdOrder.getStatus());
        assertEquals(order1.getEndDate(), createdOrder.getEndDate());
        assertNotNull(createdOrder);

    }

    @Test
    public void F3_createOrder_OrderIsValid_OrderWillBePersisted() throws Exception {

        order1 = Order.OrderBuilder.aOrder()
            .withId(Long.MAX_VALUE)
            .withStatus(Status.ERLEDIGT)
            .withAssistance("assistance")
            .withStartDate(null)
            .withEndDate(null)
            .build();


        Order createdOrder = orderService.createOrder(order1);
        assertEquals(order1.getStatus(), createdOrder.getStatus());
        assertEquals(order1.getEndDate(), createdOrder.getEndDate());
        assertNotNull(createdOrder);

    }

    @Test
    public void F4_deleteOrder_idExistsAlready_orderIsDeleted() throws Exception {

        Order newOrder = orderService.createOrder(order1);
        orderService.deleteOrder(newOrder.getId());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            orderService.deleteOrder(newOrder.getId());
        });
    }

    @Test
    public void F5_deleteOrder_idDoesntExistsAlready_EmptyResultDataAccessExceptionIsThrown() throws Exception {

        assertThrows(EmptyResultDataAccessException.class, () -> {
            orderService.deleteOrder(1000L);
        });
    }

    @Test
    public void F6_FindAnOrderById_ShouldBeSuccessful() throws Exception {
       assertEquals(orderRepository.findById(orderRepository.findAll().get(0).getId()).get(), orderService.findOne(orderRepository.findAll().get(0).getId()));
    }

    @Test
    public void F7_findOne_invalidNonNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            orderService.findOne(0L);
        });
    }

    @Test
    public void F8_findOne_invalidNegativeId_ValidationExceptionIsThrown() throws Exception {
        assertThrows(ValidationException.class, () -> {
            orderService.findOne(-1L);
        });
    }

    @Test
    public void F9_findOne_idDoesntExistsAlready_NoSuchElementExceptionIsThrown() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            orderService.findOne(200L);
        });
    }

    @Test
    public void F10_findAll_OnePersistedOrder_AllOrdersAreReturned() throws Exception {
        order1 = Order.OrderBuilder.aOrder()
            .withId(Long.MAX_VALUE)
            .withStatus(Status.ERLEDIGT)
            .withAssistance("assistance")
            .withStartDate(null)
            .withEndDate(null)
            .build();
        order1 = orderService.createOrder(order1);
        assertEquals(1, orderService.findAll().size());
    }

    @Test
    public void F11_findAll_ThreePersistedOrders_AllOrdersAreReturned() throws Exception {
        order1 = Order.OrderBuilder.aOrder()
            .withId(Long.MAX_VALUE)
            .withStatus(Status.ERLEDIGT)
            .withAssistance("assistance")
            .withStartDate(null)
            .withEndDate(null)
            .build();

        orderService.createOrder(order1);
        orderService.createOrder(order1);
        orderService.createOrder(order1);

        assertEquals(3, orderService.findAll().size());
    }

    @Test
    public void F12_findAll_NoPersistedOrder_emptyListReturned() throws Exception {
        orderService.deleteOrder(order1.getId());
        orderService.deleteOrder(order2.getId());

        List<Order> orders = orderService.findAll();
        assertTrue(orders.isEmpty());
    }

    @Test
    public void F13_SetAssistanceWithInvalidId_ShouldThrow() throws Exception {
        assertThrows(ValidationException.class, () -> {
            orderService.setAssistance(-1L, "Please Waiter Help me");
        });
    }

    @Test
    public void F14_SetAssistanceWithInvalidId_ShouldThrow() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            orderService.setAssistance(456456L, "Please Waiter Help me");
        });
    }

    @Test
    public void F15_SetAssistanceForOrder_ShouldBeSuccessful() throws Exception {
        assertEquals("Please Waiter Help me", orderService.setAssistance(order1.getId(), "Please Waiter Help me").getAssistance());
    }


}

