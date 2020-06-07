package at.ac.tuwien.sepm.groupphase.backend.unittests.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CompleteFoodDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImagelessCategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CategoryMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.FoodMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FoodRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryEndpointTest implements TestData {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    private CompleteFoodDto foodDto;
    private Food savedFood;
    private Category cat1;
    private Category cat2;
    private Category catNotShown;
    private CategoryDto catDto1;
    private CategoryDto catDto2;
    private CategoryDto catNotShownDto;
    private ImagelessCategoryDto noImCatDto1;
    private ImagelessCategoryDto noImcatDto2;
    private ImagelessCategoryDto noImcatNotShownDto;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {
        cat1 = Category.CategoryBuilder.aCategory()
            .withName("TestCategory1")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        cat2 = Category.CategoryBuilder.aCategory()
            .withName("TestCategory2")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        catNotShown = Category.CategoryBuilder.aCategory()
            .withName("TestCategory not shown")
            .withDescription("")
            .withIsShown(true)
            .withImage("UE5HDQoaCgAAAA1JSERSAAAAAQAAAAEIAgAAAHdTAAAADElEQVQIYz8ABQJZAAAAAElFTkRCYA==")
            .withImageContentType("image/jpeg")
            .build();

        cat1 = categoryRepository.save(cat1);
        cat2 = categoryRepository.save(cat2);
        catNotShown = categoryRepository.save(catNotShown);

        catDto1 = categoryMapper.categoryToCategoryDto(cat1);
        catDto2 = categoryMapper.categoryToCategoryDto(cat2);
        catNotShownDto = categoryMapper.categoryToCategoryDto(catNotShown);

        noImCatDto1 = categoryMapper.categoryToImagelessCategoryDto(cat1);
        noImcatDto2 = categoryMapper.categoryToImagelessCategoryDto(cat2);
        noImcatNotShownDto = categoryMapper.categoryToImagelessCategoryDto(catNotShown);

        List<Long> catIds = new ArrayList<>();
        catIds.add(catDto1.getId());
        catIds.add(catDto2.getId());
        catIds.add(catNotShownDto.getId());

        foodDto = CompleteFoodDto.CompleteFoodDtoBuilder.aCompleteFoodDto()
            .withName("Testname")
            .withDescription("Desc")
            .withPrice(new BigDecimal("10.0"))
            .withImage("ABBKRklGAAEBAQBIAEgAAAATQ3JlYXRlZCB3aXRoIEdJTVAAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDRcYFhQYEhQVFABDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQAEQgAAQABAwERAAIRAQMRAQAUAAEAAAAAAAAAAAAAAAAAAAAIABQBAQAAAAAAAAAAAAAAAAAAAAAADAMBAAIQAxAAAAFUABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAEFAn8AFBEBAAAAAAAAAAAAAAAAAAAAAAAIAQMBAT8BfwAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAgEBPwF/ABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAY/An8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8hfwAMAwEAAgADAAAAEAAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAwEBPxB/ABQRAQAAAAAAAAAAAAAAAAAAAAAACAECAQE/EH8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8Qfw==")
            .withImageContentType("image/jpeg")
            .withCategoryIds(catIds)
            .build();

        savedFood = foodMapper.completeFoodDtoToFood(foodDto);
        savedFood.setId(foodRepository.save(savedFood).getId());
        foodDto.setId(savedFood.getId());
    }

    @AfterEach
    public void afterEach(){
        foodRepository.delete(savedFood);
    }

    @Test
    public void improvisedTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(CATEGORY_BASE_URI + "/" + foodDto.getId())
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}
