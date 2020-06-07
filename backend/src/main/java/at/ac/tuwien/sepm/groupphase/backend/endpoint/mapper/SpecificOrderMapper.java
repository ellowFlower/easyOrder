package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import at.ac.tuwien.sepm.groupphase.backend.service.DrinkService;
import at.ac.tuwien.sepm.groupphase.backend.service.FoodService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class SpecificOrderMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private OrderService orderService;

    public Order OrderDtoToOrder(OrderDto orderDto){



        Order order = new Order();

        order.setId(orderDto.getId());
        order.setAssistance(orderDto.getAssistance());
        order.setStartDate(orderDto.getStartDate());
        order.setEndDate(orderDto.getEndDate());
        if(orderDto.getStatus().equals("NEU")){
            order.setStatus(Status.NEU);
        }
        else if(orderDto.getStatus().equals("ERLEDIGT")){
            order.setStatus(Status.ERLEDIGT);
        }
        else if(orderDto.getStatus().equals("SERVIEREN")){
            order.setStatus(Status.SERVIEREN);
        }

        order.setTable(userService.findOne(orderDto.getTableId()));

        List<Integer> fo = orderDto.getFoodsId();
        for (int i = 0; i < fo.size(); i++) {
            if (fo.get(i) != null) {
                Long help = new Long(fo.get(i));
                order.getFoods().add(foodService.findOne(help));
            }
        }

        List<Integer> dr = orderDto.getDrinksId();
        for (int i = 0; i < dr.size(); i++) {
            if (dr.get(i) != null) {
                Long help = new Long(dr.get(i));
                order.getDrinks().add(drinkService.findOne(help));
            }
        }



        return order;
    }


    public SimpleOrderDto OrderToSimpleOrderDto(Order order) {
        SimpleOrderDto cmplDto = new SimpleOrderDto();
        cmplDto.setId(order.getId());
        cmplDto.setAssistance(order.getAssistance());
        cmplDto.setStartDate(order.getStartDate());
        cmplDto.setEndDate(order.getEndDate());
        cmplDto.setStatus(order.getStatus());
        cmplDto.setTableId(order.getTable().getId());

        for (Food f : order.getFoods()) {
            SimpleFoodDto fDto = new SimpleFoodDto();

            fDto.setId(f.getId());
            fDto.setName(f.getName());
            fDto.setDescription(f.getDescription());
            fDto.setPrice(f.getPrice());

            cmplDto.getFoods().add(fDto);
        }

        for (Drink d : order.getDrinks()) {
            SimpleDrinkDto dDto = new SimpleDrinkDto();

            dDto.setId(d.getId());
            dDto.setName(d.getName());
            dDto.setDescription(d.getDescription());
            dDto.setPrice(d.getPrice());
            dDto.setAlcohol(d.getAlcohol());

            cmplDto.getDrinks().add(dDto);
        }

        return cmplDto;
    }

    public List<PayDto> OrdersToPayDto(List<Order> orders) {

        List<PayDto> listPay = new ArrayList<>();

        List<Long> orderTableIds = orderService.getOrderTableIds();

        for (Long id : orderTableIds) {
            List<Order> listOrders = orderService.getOrdersByTable(id);
            PayDto pay = new PayDto();

            for (Order o : listOrders) {
                pay.setTableId(o.getTable().getId());
                for (Food f : o.getFoods()) {
                    pay.addFood(f);
                    pay.addPrice(f.getPrice());
                }
                for (Drink d : o.getDrinks()) {
                    pay.addDrink(d);
                    pay.addPrice(d.getPrice());
                }
            }

            // check if order exists
            if (pay.getTableId() != null) {
                listPay.add(pay);
            }
        }

        return listPay;
    }


}
