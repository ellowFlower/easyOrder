package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query(value = "select f from Food f where lower(name) LIKE %:name% and updated=false and deleted=false ")
    public List<Food> findAllByNameContaining(@Param("name") String name);

    List<Food> findByDeleted(Boolean deleted);

    List<Food> findByUpdated(Boolean deleted);

    List<Food> findByDeletedAndUpdated(Boolean deleted, Boolean updated);

}
