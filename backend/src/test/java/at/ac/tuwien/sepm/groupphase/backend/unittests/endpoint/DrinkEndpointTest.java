package at.ac.tuwien.sepm.groupphase.backend.unittests.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DrinkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CategoryMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DrinkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.repository.CategoryRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.DrinkRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DrinkEndpointTest implements TestData {

    @Autowired
    private DrinkRepository drinkRepository;
    @Autowired
    private DrinkMapper drinkMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    private DrinkDto drinkDto;
    private Drink savedDrink;
    private Category cat1;
    private Category cat2;
    private CategoryDto catDto1;
    private CategoryDto catDto2;

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

        cat1 = categoryRepository.save(cat1);
        cat2 = categoryRepository.save(cat2);

        catDto1 = categoryMapper.categoryToCategoryDto(cat1);
        catDto2 = categoryMapper.categoryToCategoryDto(cat2);

        List<Long> catIds = new ArrayList<>();
        catIds.add(catDto1.getId());
        catIds.add(catDto2.getId());

        drinkDto = DrinkDto.DrinkDtoBuilder.aDrinkDto()
            .withName("Testname")
            .withDescription("Desc")
            .withPrice(new BigDecimal("10.0"))
            .withImage("ABBKRklGAAEBAQBIAEgAAAATQ3JlYXRlZCB3aXRoIEdJTVAAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDRcYFhQYEhQVFABDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQAEQgAAQABAwERAAIRAQMRAQAUAAEAAAAAAAAAAAAAAAAAAAAIABQBAQAAAAAAAAAAAAAAAAAAAAAADAMBAAIQAxAAAAFUABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAEFAn8AFBEBAAAAAAAAAAAAAAAAAAAAAAAIAQMBAT8BfwAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAgEBPwF/ABQQAQAAAAAAAAAAAAAAAAAAAAAACAEBAAY/An8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8hfwAMAwEAAgADAAAAEAAUEQEAAAAAAAAAAAAAAAAAAAAAAAgBAwEBPxB/ABQRAQAAAAAAAAAAAAAAAAAAAAAACAECAQE/EH8AFBABAAAAAAAAAAAAAAAAAAAAAAAIAQEAAT8Qfw==")
            .withImageContentType("image/jpeg")
            .withAlcohol(5)
            .withCategoryIds(catIds)
            .withDeleted(false)
            .withUpdated(false)
            .build();

        savedDrink = drinkMapper.drinkDtoToDrink(drinkDto);
        savedDrink.setId(drinkRepository.save(savedDrink).getId());
        drinkDto.setId(savedDrink.getId());
    }

    @AfterEach
    public void afterEach(){
        drinkRepository.delete(savedDrink);
    }

    @Test
    public void EPD1_getAll_noDrinksPersisted_then200() throws Exception {
        drinkRepository.delete(savedDrink);

        MvcResult mvcResult = this.mockMvc.perform(get(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        savedDrink = drinkMapper.drinkDtoToDrink(drinkDto);
        savedDrink.setId(drinkRepository.save(savedDrink).getId());
        drinkDto.setId(savedDrink.getId());
    }

    @Test
    public void EPD2_getAll_DrinksPersisted_then200() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD3_findOneById_whenNotFound_then404() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(DRINK_BASE_URI + "/5555")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void EPD4_findOneById_invalidID_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(DRINK_BASE_URI + "/-1")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    public void EPD5_findOneById_whenFound_then200() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(DRINK_BASE_URI + "/" + drinkDto.getId())
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content("1L"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD6_findOneById_unauthorized_then401() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(DRINK_BASE_URI + "/" + drinkDto.getId())
            .header(securityProperties.getAuthHeader(), "")
            .contentType(MediaType.APPLICATION_JSON)
            .content("1L"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void EPD7_post_whenCreated_then201() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }

    @Test
    public void EPD8_post_invalidDrink_then422() throws Exception {
        drinkDto.setAlcohol(-1);

        MvcResult mvcResult = this.mockMvc.perform(post(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test void EPD9_post_whenNullObj_then400() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test void EPD10_post_WhenNotAuthorized_then401() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(DRINK_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test void EPD11_post_authorizedAsUser_then401() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void EPD12_delete_whenDeleted_then200() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(DRINK_BASE_URI + "/" + drinkDto.getId())
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD13_delete_whenInvalid_then422() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(DRINK_BASE_URI + "/-5555")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    public void EPD14_delete_nonexistingId_then404() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(DRINK_BASE_URI + "/5555")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void EPD15_delete_whenNotAuthorized_then401() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(DRINK_BASE_URI + "/" + drinkDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void EPD16_delete_authorizedAsUser_then403() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(DRINK_BASE_URI + "/" + drinkDto.getId())
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void EPD17_patch_whenPatched_then200() throws Exception {
        drinkDto.setName("newname");
        MvcResult mvcResult = this.mockMvc.perform(patch(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD18_patch_nonexistingId_then404() throws Exception {
        drinkDto.setId(5555L);
        MvcResult mvcResult = this.mockMvc.perform(patch(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void EPD19_patch_whenInvalid_then400() throws Exception {
        drinkDto.setName("newName");
        drinkDto.setAlcohol(-1);

        MvcResult mvcResult = this.mockMvc.perform(patch(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void EPD20_patch_emptyBody_then400() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(patch(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void EPD21_patch_whenNotAutorized_then401() throws Exception {
        drinkDto.setName("newname");
        MvcResult mvcResult = this.mockMvc.perform(patch(DRINK_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void EPD22_patch_authorizedAsUser_then403() throws Exception {
        drinkDto.setName("newname");
        MvcResult mvcResult = this.mockMvc.perform(patch(DRINK_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(drinkDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }
}

