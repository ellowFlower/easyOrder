package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    /**
     * Save a order. The order contains all foods and drinks if there are some
     *
     * @param order to save
     * @return saved order entry
     */
    Order createOrder(Order order);

    /**
     * delete a specific order
     *
     * @param id of the order to be deleted
     * @return deleted order entry
     */
    void deleteOrder(Long id);

    /**
     * get a order by its ID
     *
     * @param id to be returned
     * @return order with specified id
     */
    Order findOne(Long id);

    /**
     * get all saved orders without status ERLEDIGT and
     * without assistance 'Bitte Zahlen', 'Bitte um Besteck' and 'Bitte um Hilfe'
     *
     * @return list of orders
     */
    List<Order> findAll();

    /**
     * get all saved orders with status NEU
     *
     * @return list of orders
     */
    List<Order> findNew();

    /**
     * get all saved orders with status SERVIEREN
     *
     * @return list of orders
     */
    List<Order> findServe();

    /**
     * get all saved orders with status ERLEDIGT
     *
     * @return list of orders
     */
    List<Order> findDone();

    /**
     * get ids of the tables of the orders without status ERLEDIGT and
     * without assistance 'Bitte Zahlen', 'Bitte um Besteck' and 'Bitte um Hilfe'
     *
     * @return list of ids
     */
    List<Long> getOrderTableIds();

    /**
     * get orders with matching table and not status ERLEDIGT
     *
     * @return list of orders
     */
    List<Order> getOrdersByTable(Long id);

    /**
     * set the status of an order
     *
     * @param id of order
     * @param status to set
     * @return order after the patch
     */
    Order setStatus(Long id, String status);

    /**
     * set the status of orders on a table
     *
     * @param tableId of orders
     * @return list of updated orders
     */
    List<Order> setStatusPerTable(Long tableId);

    /**
     * set the assistance of a order
     *
     * @param id of order
     * @param assistance Text to set
     * @return order after the patch
     */
    Order setAssistance(Long id, String assistance);

}
