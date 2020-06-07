package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStatisticDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatisticDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.StatisticService;
import at.ac.tuwien.sepm.groupphase.backend.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ImplStatisticService implements StatisticService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderService orderService;
    private final Validator validator;

    @Autowired
    public ImplStatisticService(OrderService orderService, Validator validator) {
        this.orderService = orderService;
        this.validator = validator;
    }

    @Override
    public StatisticDto overallTurnover(){
        this.LOGGER.debug("get turnover statistic about all orders");
        Date firstOrder = new Date();
        List<ItemStatisticDto> foodStatistics = new ArrayList<ItemStatisticDto>();
        List<ItemStatisticDto> drinkStatistics = new ArrayList<ItemStatisticDto>();

        // get all orders with status ERLEDIGT.
        List<Order> orders = this.orderService.findDone();

        // iterate over all orders while adaption stat.
        for (Order order: orders){
            List<Food> foodList = order.getFoods();
            List<Drink> drinkList = order.getDrinks();

            // update start-date
            Date orderDate = order.getEndDate();
            if (orderDate.before(firstOrder)) {
                firstOrder = new Date(orderDate.getTime());
            }

            // look at food
            for (Food food: foodList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto foodStat = this.getItemStatByName(foodStatistics, food);
                // update food value and sales
                foodStat.setValue(foodStat.getValue().add(food.getPrice()));
            }

            // look at drinks
            for (Drink drink: drinkList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto drinkStat = this.getItemStatByName(drinkStatistics, drink);
                // update food value and sales
                drinkStat.setValue(drinkStat.getValue().add(drink.getPrice()));
            }
        }

        return new StatisticDto(firstOrder, foodStatistics, drinkStatistics);
    }

    @Override
    public StatisticDto overallSales(){
        this.LOGGER.debug("get sales statistic about all orders");
        Date firstOrder = new Date();
        List<ItemStatisticDto> foodStatistics = new ArrayList<ItemStatisticDto>();
        List<ItemStatisticDto> drinkStatistics = new ArrayList<ItemStatisticDto>();

        // get all orders with status ERLEDIGT.
        List<Order> orders = this.orderService.findDone();

        // iterate over all orders while adaption stat.
        for (Order order: orders){
            List<Food> foodList = order.getFoods();
            List<Drink> drinkList = order.getDrinks();

            // update start-date
            Date orderDate = order.getEndDate();
            if (orderDate.before(firstOrder)) {
                firstOrder = new Date(orderDate.getTime());
            }

            // look at food
            for (Food food: foodList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto foodStat = this.getItemStatByName(foodStatistics, food);
                // update food value and sales
                foodStat.setValue(foodStat.getValue().add(new BigDecimal(1)));
            }

            // look at drinks
            for (Drink drink: drinkList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto drinkStat = this.getItemStatByName(drinkStatistics, drink);
                // update food value and sales
                drinkStat.setValue(drinkStat.getValue().add(new BigDecimal(1)));
            }
        }

        return new StatisticDto(firstOrder, foodStatistics, drinkStatistics);
    }


    @Override
    public StatisticDto timedTurnoverStat(Date from, Date to){
        this.LOGGER.debug("get statistic about all orders from {} to {}", from, to);
        this.validator.validateStatisticDates(from, to);
        List<ItemStatisticDto> foodStatistics = new ArrayList<ItemStatisticDto>();
        List<ItemStatisticDto> drinkStatistics = new ArrayList<ItemStatisticDto>();

        // get all orders with status ERLEDIGT.
        List<Order> orders = this.orderService.findDone();

        // iterate over all orders while adaption stat.
        for (Order order: orders){
            Date orderDate = order.getEndDate();
            if (orderDate.before(from) || orderDate.after(to)){
                continue;
            }

            List<Food> foodList = order.getFoods();
            List<Drink> drinkList = order.getDrinks();

            // look at food
            for (Food food: foodList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto foodStat = this.getItemStatByName(foodStatistics, food);
                // update food value and sales
                foodStat.setValue(foodStat.getValue().add(food.getPrice()));
            }

            // look at drinks
            for (Drink drink: drinkList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto drinkStat = this.getItemStatByName(drinkStatistics, drink);
                // update food value and sales
                drinkStat.setValue(drinkStat.getValue().add(drink.getPrice()));
            }
        }

        return new StatisticDto(from, to, foodStatistics, drinkStatistics);
    }

    @Override
    public StatisticDto timedSalesStat(Date from, Date to){
        this.LOGGER.debug("get statistic about all orders from {} to {}", from, to);
        this.validator.validateStatisticDates(from, to);
        List<ItemStatisticDto> foodStatistics = new ArrayList<ItemStatisticDto>();
        List<ItemStatisticDto> drinkStatistics = new ArrayList<ItemStatisticDto>();

        // get all orders with status ERLEDIGT.
        List<Order> orders = this.orderService.findDone();

        // iterate over all orders while adaption stat.
        for (Order order: orders){
            Date orderDate = order.getEndDate();
            if (orderDate.before(from) || orderDate.after(to)){
                continue;
            }

            List<Food> foodList = order.getFoods();
            List<Drink> drinkList = order.getDrinks();

            // look at food
            for (Food food: foodList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto foodStat = this.getItemStatByName(foodStatistics, food);
                // update food value and sales
                foodStat.setValue(foodStat.getValue().add(new BigDecimal(1)));
            }

            // look at drinks
            for (Drink drink: drinkList){
                // get Food-Statistic and add food to list if not already in it
                ItemStatisticDto drinkStat = this.getItemStatByName(drinkStatistics, drink);
                // update food value and sales
                drinkStat.setValue(drinkStat.getValue().add(new BigDecimal(1)));
            }
        }

        return new StatisticDto(from, to, foodStatistics, drinkStatistics);
    }

    private ItemStatisticDto getItemStatByName(List<ItemStatisticDto> statList, Food food){
        for (ItemStatisticDto itemStat: statList){
            if (food.getName().equals(itemStat.getName())){
                return itemStat;
            }
        }
        // name is not in the list
        // create new Statistic and add it to the list
        ItemStatisticDto newStat = new ItemStatisticDto(food);
        statList.add(newStat);
        return newStat;
    }

    private ItemStatisticDto getItemStatByName(List<ItemStatisticDto> statList, Drink drink){
        for (ItemStatisticDto itemStat: statList){
            if (drink.getName().equals(itemStat.getName())){
                return itemStat;
            }
        }
        // name is not in the list
        // create new Statistic and add it to the list
        ItemStatisticDto newStat = new ItemStatisticDto(drink);
        statList.add(newStat);
        return newStat;
    }
}
