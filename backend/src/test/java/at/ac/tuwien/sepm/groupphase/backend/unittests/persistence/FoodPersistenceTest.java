package at.ac.tuwien.sepm.groupphase.backend.unittests.persistence;


import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FoodPersistenceTest {

    private FoodRepository foodRepository;
    private Food food;
    private Food invalidFood;
    private Food nullFood = null;

    @BeforeEach
    public void beforeEach() {
        food = Food.FoodBuilder.aFood()
            .withId(1L)
            .withName("Testname")
            .withDescription("Desc")
            .withPrice(new BigDecimal(10.0))
            .withImage(null)
            .withDeleted(false)
            .withUpdated(false)
            .build();

        invalidFood = Food.FoodBuilder.aFood()
            .withId(-1L)
            .withName("")
            .withDescription("")
            .withPrice(new BigDecimal(-1.0))
            .withImage(null)
            .withDeleted(false)
            .withUpdated(false)
            .build();
    }

    @Test
    public void giveFood_whenCreated_addedFood() throws Exception {


    }


    //ein positiv test ein negativ Test ein Nulltest
}

