package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStatisticDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatisticDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StatisticServiceTest {

    @Autowired
    StatisticService statisticService;
    @Autowired
    OrderService orderService;
    @Autowired
    FoodService foodService;
    @Autowired
    DrinkService drinkService;
    @Autowired
    UserService userService;

    StatisticDto statisticDto;
    Food food1;
    Food food2;
    Drink drink1;
    Drink drink2;
    Order order;

    @BeforeEach
    public void beforeEach() throws ParseException {
        food1 = Food.FoodBuilder.aFood()
            .withId(1L)
            .withName("Food1")
            .withDescription("Description1")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withCategories(new ArrayList<>())
            .build();

        food2 = Food.FoodBuilder.aFood()
            .withId(2L)
            .withName("Food2")
            .withDescription("Description2")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withCategories(new ArrayList<>())
            .build();

        drink1 = Drink.DrinkBuilder.aDrink()
            .withId(1L)
            .withName("Drink1")
            .withDescription("Description")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withAlcohol(0)
            .withAssignedCategories(new ArrayList<>())
            .build();

        drink2 = Drink.DrinkBuilder.aDrink()
            .withId(1L)
            .withName("Drink2")
            .withDescription("Description")
            .withPrice(new BigDecimal("10.00"))
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/png")
            .withAlcohol(0)
            .withAssignedCategories(new ArrayList<>())
            .build();


        food1 = foodService.saveFood(food1);
        drink1 = drinkService.saveDrink(drink1);

        List<Food> foodList = new ArrayList<>();
        foodList.add(food1);
        //foodList.add(food2);

        List<Drink> drinkList = new ArrayList<>();
        drinkList.add(drink1);
        //drinkList.add(drink2);

        order = Order.OrderBuilder.aOrder()
            .withAssistance("")
            .withStartDate(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("01-01-2020 00:00"))
            .withEndDate(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("01-01-2020 05:00"))
            .withId(1L)
            .withStatus(Status.ERLEDIGT)
            .withDrinks(drinkList)
            .withFoods(foodList)
            .build();

        order = orderService.createOrder(order);
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

        try {
            List<Drink> drinks = drinkService.findAll();
            for (Drink drink : drinks) {
                Long id = drink.getId();
                drinkService.deleteDrink(id);
            }
        } catch (NotFoundException ignored) {
        }

        try {
            List<Order> orders = orderService.findAll();
            for (Order order : orders) {
                Long id = order.getId();
                orderService.deleteOrder(id);
            }
        } catch (NotFoundException ignored) {
        }
    }


    /*
    @Test
    public void T1_FoodPriceIsZeroAndDrinkOrderedMultipleTimes_StatisticIsCreated() throws ParseException {
        // persist order accordingly
        food1.setPrice(new BigDecimal(0));
        List<Food> foodList = new ArrayList<>();
        foodList.add(food1);
        foodList.add(food2);
        order.setFoods(foodList);
        List<Drink> drinkList = new ArrayList<>();
        drinkList.add(drink1);
        drinkList.add(drink1);
        order.setDrinks(drinkList);

        foodService.saveFood(food1);
        foodService.saveFood(food2);
        drinkService.saveDrink(drink1);
        Long persistedId = orderService.createOrder(order).getId();
        orderService.setStatus(persistedId, "ERLEDIGT");
        statisticDto = statisticService.overall();

        assertEquals(statisticDto.getStart(), new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("01-01-2020 05:00"));
        //assertEquals(statisticDto.getTurnover(), new BigDecimal("30.00"));
        //assertEquals(statisticDto.getSales(), 4L);

        ItemStatisticDto foodStat1 = new ItemStatisticDto("Food1", new BigDecimal("0.00"), 1L);
        ItemStatisticDto foodStat2 = new ItemStatisticDto("Food2", new BigDecimal("10.00"), 1L);
        ItemStatisticDto drinkStat1 = new ItemStatisticDto("Drink1", new BigDecimal("20.00"), 2L);

        assertTrue(statisticDto.getFoodStatistics().contains(foodStat1));
        assertTrue(statisticDto.getFoodStatistics().contains(foodStat2));
        assertTrue(statisticDto.getDrinkStatistics().contains(drinkStat1));

        orderService.deleteOrder(persistedId);
    }

    */


    @Test
    public void T2_AllFoodAndDrinkPricesAreZero_StatisticIsCreated() {

    }

    @Test
    public void T3_OrderIsEmptyOrderIsNotErledigt_NotErledigtOrderNotConsidered() {

    }

    @Test
    public void T4_OnlyOrderIsEmpty_StatisticIsCreated() {

    }

    @Test
    public void T5_NoOrders_StatisticIsCreated() {

    }

    @Test
    public void T6_DeletedFoodAndUpdatedDrink_StatisticConsideresFoodAndDrink() {

    }

    @Test
    public void F1_createTurnoverStatistic_statsNull_returnStatistic() {

        StatisticDto stats = statisticService.overallTurnover();
        assertEquals(stats.getfoodStats().size(), 0);
        assertEquals(stats.getdrinkStats().size(), 0);
    }

    @Test
    public void F2_createTurnoverStatistic_oneDrinkAndFood_returnStatistic() {

        food1.setPrice(new BigDecimal(0));
        List<Food> foodList = new ArrayList<>();
        foodList.add(food1);
        foodList.add(food2);
        order.setFoods(foodList);
        List<Drink> drinkList = new ArrayList<>();
        drinkList.add(drink1);
        drinkList.add(drink1);
        order.setDrinks(drinkList);

        foodService.saveFood(food1);
        drinkService.saveDrink(drink1);
        Long persistedId = orderService.createOrder(order).getId();
        orderService.setStatus(persistedId, "ERLEDIGT");

        StatisticDto stats = statisticService.overallTurnover();
        assertEquals(stats.getfoodStats().size(), 1);
        assertEquals(stats.getdrinkStats().size(), 1);
    }

    @Test
    public void F3_createSaleStatistic_statsNull_returnSales() {
        orderService.deleteOrder(order.getId());

        StatisticDto stats = statisticService.overallSales();
        assertEquals(stats.getfoodStats().size(), 1);
        assertEquals(stats.getdrinkStats().size(), 1);
    }

    @Test
    public void F4_createSaleStatistic_oneDrinkAndFood_returnSales() {

        orderService.deleteOrder(order.getId());

        food1.setPrice(new BigDecimal(0));
        List<Food> foodList = new ArrayList<>();
        foodList.add(food1);
        foodList.add(food2);
        order.setFoods(foodList);
        List<Drink> drinkList = new ArrayList<>();
        drinkList.add(drink1);
        drinkList.add(drink1);
        order.setDrinks(drinkList);

        foodService.saveFood(food1);
        drinkService.saveDrink(drink1);
        Long persistedId = orderService.createOrder(order).getId();
        orderService.setStatus(persistedId, "ERLEDIGT");

        StatisticDto stats = statisticService.overallSales();
        assertEquals(stats.getfoodStats().size(), 1);
        assertEquals(stats.getdrinkStats().size(), 1);
        BigDecimal sales = stats.getdrinkStats().get(0).getValue();
        assertEquals(new BigDecimal(4), sales);
        sales = stats.getdrinkStats().get(0).getValue();
        assertEquals(new BigDecimal(4), sales);
    }

    @Test
    public void F5_timedTurnoverStat_validDates_returnTurnover() {

        Date from = order.getStartDate();
        Date to = order.getEndDate();
        StatisticDto stats = statisticService.timedTurnoverStat(from,to);
        assertEquals( 0, stats.getfoodStats().size());
        assertEquals( 0, stats.getdrinkStats().size());
    }

    @Test
    public void F6_timedTurnoverStat_invalidDates_returnValidationException() {

        Date from = order.getStartDate();
        Date to = order.getEndDate();
        assertThrows(ValidationException.class, () -> {
            statisticService.timedTurnoverStat(to,from);
        });
    }

    @Test
    public void F7_timedSales_validDates_returnTurnover() {

        Date from = order.getStartDate();
        Date to = order.getEndDate();
        StatisticDto stats = statisticService.timedSalesStat(from,to);
        assertEquals( 0, stats.getfoodStats().size());
        assertEquals( 0, stats.getdrinkStats().size());
    }

    @Test
    public void F8_timedSales_invalidDates_returnValidationException() {

        Date from = order.getStartDate();
        Date to = order.getEndDate();
        assertThrows(ValidationException.class, () -> {
            statisticService.timedSalesStat(to,from);
        });
    }
}
