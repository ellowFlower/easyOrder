package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.repository.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Profile("generateData")
@Component
public class FoodDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_FOODS_TO_GENERATE = 5;
    private static final String TEST_NAME = "Name";
    private static final String TEST_DESCRIPTION = "Description of the food";
    private static final BigDecimal TEST_PRICE = new BigDecimal(5.0);

    private final FoodRepository foodRepository;

    public FoodDataGenerator(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    // test data without an image !!!!!
    @PostConstruct
    private void generateFood() {
        if (foodRepository.findAll().size() > 0) {
            LOGGER.debug("food already generated");
        } else {
            LOGGER.debug("generating {} food entries", NUMBER_OF_FOODS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_FOODS_TO_GENERATE; i++) {
                Food food = Food.FoodBuilder.aFood()
                    .withName(TEST_NAME + " " + i)
                    .withDescription(TEST_DESCRIPTION + " " + i)
                    .withPrice(TEST_PRICE.add(BigDecimal.valueOf(i)))
                    .build();
                LOGGER.debug("saving food {}", food);
                foodRepository.save(food);
            }
        }
    }
}
