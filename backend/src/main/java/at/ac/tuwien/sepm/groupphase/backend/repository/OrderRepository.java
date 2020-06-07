package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where (o.status = 'NEU' or o.status = 'SERVIEREN' or o.status = 'SERVIERT') and o.assistance != 'Bitte Zahlen' and o.assistance != 'Bitte um Besteck' and o.assistance != 'Bitte um Hilfe'")
    List<Order> getOrdersByStatusAndByAssistance();

    List<Order> findByStatus(Status neu);

    @Query("select o from Order o where o.table.id = ?1 and (o.status = 'NEU' or o.status = 'SERVIEREN' or o.status = 'SERVIERT') and o.assistance != 'Bitte Zahlen' and o.assistance != 'Bitte um Besteck' and o.assistance != 'Bitte um Hilfe'")
    List<Order> getOrdersWithTable(Long id);

    @Query("select o.table.name from Order o where o.status = 'NEU' or o.status = 'SERVIEREN' or o.status = 'SERVIERT'")
    List<String> getTableNames();

    @Query("select o from Order o where o.table.id = ?1 and o.status = 'ERLEDIGT'")
    List<Order> getOrdersWithTableId(Long id);
}
