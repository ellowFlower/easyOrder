package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImplOrderService implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderRepository orderRepository;
    private final Validator validator;

    @Autowired
    public ImplOrderService(OrderRepository orderRepository, Validator validator) {
        this.orderRepository = orderRepository;
        this.validator = validator;
    }


    @Override
    public Order createOrder(Order order) {
        LOGGER.info("Save new order {}", order);
        //if ID is given, it is set to null, so that no existing entity can be overwritten
        order.setId(null);

        // set status automatically at the begin
        order.setStatus(Status.NEU);

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        LOGGER.debug("Delete order with id {}", id);

        validator.validateId(id);

        orderRepository.deleteById(id);
    }

    @Override
    public Order findOne(Long id) {
        LOGGER.debug("Get order with id {}", id);

        validator.validateId(id);

        Optional<Order> order = orderRepository.findById(id);

        return order.get();
    }

    @Override
    public List<Order> findAll() {
        LOGGER.debug("Get all saved orders without status ERLEDIGT");
        return orderRepository.getOrdersByStatusAndByAssistance();
    }

    @Override
    public List<Order> findNew() {
        LOGGER.debug("Get all saved orders with status NEU");
        return orderRepository.findByStatus(Status.NEU);
    }

    @Override
    public List<Order> findServe() {
        LOGGER.debug("Get all saved orders with status SERVIEREN");
        return orderRepository.findByStatus(Status.SERVIEREN);
    }

    @Override
    public List<Order> findDone() {
        LOGGER.debug("Get all saved orders with status ERLEDIGT");
        return orderRepository.findByStatus(Status.ERLEDIGT);
    }

    @Override
    public List<Long> getOrderTableIds() {
        LOGGER.debug("Get all tableIds from saved orders");
        List<Order> orders = orderRepository.getOrdersByStatusAndByAssistance();

        List<Long> ids = new ArrayList<>();
        for (Order o : orders) {
            if (!ids.contains(o.getTable().getId())) {
                ids.add(o.getTable().getId());
            }
        }

        return ids;
    }

    @Override
    public List<Order> getOrdersByTable(Long id) {
        LOGGER.debug("Get orders with specific table and without status ERLEDIGT.");
        return orderRepository.getOrdersWithTable(id);
    }


    @Override
    public Order setStatus(Long id, String status) {
        LOGGER.debug("Set status for order with id: {}", id);
        validator.validateId(id);
        validator.validateOrderStatus(status);

        Order order = findOne(id);

        // Because we check in the validator if the given string status is a correct Status
        // we now know that the string exists in our enum type and we can translate it
        order.setStatus(Status.valueOf(status));


        if (status.equals("ERLEDIGT")) {
           order.setEndDate(new Date());
        }

        orderRepository.save(order);

//=======
   //     if (order.getEndDate() == null){
   //         order.setEndDate(new Date());
   //     }
//>>>>>>> Statistiken

        orderRepository.save(order);
        return findOne(id);
    }

    @Override
    public List<Order> setStatusPerTable(Long tableId) {
        LOGGER.debug("Set status for all orders with tableId: {}", tableId);
        validator.validateId(tableId);

        List<Order> updatedOrders = new ArrayList<>();
        for (Order o : findAll()) {
            if (!(o.getStatus().equals(Status.ERLEDIGT)) && o.getTable().getId() == tableId) {
                o.setStatus(Status.ERLEDIGT);
                o.setEndDate(new Date());
                orderRepository.save(o);
                updatedOrders.add(o);
            }
        }

        return updatedOrders;
    }

    @Override
    public Order setAssistance(Long id, String assistance) {
        LOGGER.debug("Set assistance of order with id: {}", id);

        validator.validateId(id);

        Order order = findOne(id);

        order.setAssistance(assistance);
        orderRepository.save(order);


        return findOne(id);
    }


}
