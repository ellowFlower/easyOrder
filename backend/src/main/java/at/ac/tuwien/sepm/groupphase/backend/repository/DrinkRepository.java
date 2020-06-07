package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {

    @Query(value = "select f from Drink f where lower(name) LIKE %:name% and updated=false and deleted=false ")
    public List<Drink> findAllByNameContaining(@Param("name") String name);

    List<Drink> findByDeleted(Boolean deleted);

    List<Drink> findByUpdated(Boolean deleted);

    List<Drink> findByDeletedAndUpdated(Boolean deleted, Boolean updated);
}
