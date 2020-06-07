package at.ac.tuwien.sepm.groupphase.backend.unittests.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.RegistrationEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderEndpointTest implements TestData {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    SpecificOrderMapper specificOrderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TableInfoRepository tableInfoRepository;

    private CompleteFoodDto foodDto;
    private Food savedFood;
    private Drink savedDrink;
    private DrinkDto drinkDto;
    private Order order;
    private OrderDto orderDto;
    private ApplicationUser applicationUser;
    private ApplicationUser user;
    private ApplicationUser userApplication;
    private TableRegistrationDto tableInfoDto;
    private TableInfo tableInfo;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {


        userApplication = new ApplicationUser();
        userApplication.setPassword("pw");
        userApplication.setName("tablename");
        userApplication.setEmail("a");
        userApplication.setAdmintype("Table");

        tableInfo = new TableInfo();
        tableInfo.setSeats(4);
        tableInfo.setUsed(true);
        tableInfo.setApplicationUser(userApplication);
        tableInfo = tableInfoRepository.save(tableInfo);
        userApplication.setTableInfo(tableInfo);
        userApplication =  userRepository.save(userApplication);

        applicationUser = new ApplicationUser();
        applicationUser.setPassword("testtest");
        applicationUser.setName("admin@email.com");
        applicationUser.setAdmintype("Admin");
        applicationUser.setEmail("admin@email.com");
        applicationUser = userRepository.save(applicationUser);

        user = new ApplicationUser();
        user.setPassword("testtest");
        user.setName("user@email.com");
        user.setAdmintype("User");
        user.setEmail("");
        user = userRepository.save(user);



        orderDto = OrderDto.OrderDtoBuilder.aOrder()
            .withId(null)
            .withAssistance("Bitte Zahlen")
            .withStatus("NEU")
            .withStartDate(null)
            .withEndDate(null)
            .withTable(1L)
            .build();

        order = Order.OrderBuilder.aOrder()
            .withStatus(Status.NEU)
            .withAssistance(null)
            .withStartDate(null)
            .withEndDate(null)
            .withTable(userApplication)
            .build();

        orderService.createOrder(order);


    }

    @AfterEach
    public void afterEach(){

        List<Order> orders = orderRepository.findAll();
        for (Order order: orders){
            orderRepository.delete(order);
        }

        userRepository.delete(applicationUser);
        userRepository.delete(user);
        userRepository.delete(userApplication);
        tableInfoRepository.delete(tableInfo);
    }

    @Test
    public void EPD1_getAll_noOrdersPersisted_then200() throws Exception {
        orderRepository.delete(order);

        MvcResult mvcResult = this.mockMvc.perform(get(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();


        assertEquals(HttpStatus.OK.value(), response.getStatus());

        order = orderRepository.save(order);
    }

    @Test
    public void EPD2_getAll_OrdersPersisted_then200() throws Exception {
        orderService.createOrder(order);
        orderService.createOrder(order);

        MvcResult mvcResult = this.mockMvc.perform(get(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD3_findAll_unauthorized_then401() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), "")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void EPD4_getAllPayed_OrdersPersisted_then200() throws Exception {
        OrderDto orderDto = OrderDto.OrderDtoBuilder.aOrder()
            .withAssistance("Bitte Zahlen")
            .withStatus(Status.NEU.toString())
            .withTable(1L)
            .withStartDate(null)
            .withEndDate(null)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(get(ORDER_BASE_URI + "/pay")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD5_getAllServe_OrdersPersisted_then200() throws Exception {
        OrderDto orderDto = OrderDto.OrderDtoBuilder.aOrder()
            .withAssistance("Bitte Zahlen")
            .withStatus(Status.SERVIEREN.toString())
            .withTable(1L)
            .withStartDate(null)
            .withEndDate(null)
            .build();

        MvcResult mvcResult = this.mockMvc.perform(get(ORDER_BASE_URI + "/serve")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD6_getAllNew_OrdersPersisted_then200() throws Exception {
        OrderDto orderDto = OrderDto.OrderDtoBuilder.aOrder()
            .withAssistance("Bitte Zahlen")
            .withStatus(Status.NEU.toString())
            .withTable(1L)
            .withStartDate(null)
            .withEndDate(null)
            .build();


        MvcResult mvcResult = this.mockMvc.perform(get(ORDER_BASE_URI + "/new")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD7_post_whenCreated_then201() throws Exception {

        String s = "{\"id\":null, \"status\":\"NEU\", \"assistance\":\"Bitte Zahlen\", \"startDate\":null, \"endDate\":null, \"tableId\":1}";

        MvcResult mvcResult = this.mockMvc.perform(post(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("tablename", ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(s))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }

    @Test
    public void EPD8_post_invalidOrder_then422() throws Exception {

        String s = "{\"id\":null, \"status\":\"null\", \"assistance\":\"Bitte Zahlen\", \"startDate\":null, \"endDate\":null, \"tableId\":1}";

        MvcResult mvcResult = this.mockMvc.perform(post(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("tablename", ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(s))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test void EPD9_post_whenNullObj_then400() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test void EPD10_post_WhenNotAuthorized_then401() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(ORDER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test void EPD11_post_authorizedAsUser_then201() throws Exception {
        String s = "{\"id\":null, \"status\":\"NEU\", \"assistance\":\"Bitte Zahlen\", \"startDate\":null, \"endDate\":null, \"tableId\":1}";

        MvcResult mvcResult = this.mockMvc.perform(post(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(s))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void EPD12_patch_whenPatched_then200() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(patch(ORDER_BASE_URI + "?id=1&status=ERLEDIGT")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("tablename", ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD13_patch_nonexistingId_then404() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(patch(ORDER_BASE_URI + "?id=5555&status=ERLEDIGT")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("tablename", ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void EPD14_patch_whenInvalid_then400() throws Exception {
        orderDto.setAssistance("new");
        orderDto.setId(-1L);

        MvcResult mvcResult = this.mockMvc.perform(patch(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void EPD15_patch_emptyBody_then400() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(patch(ORDER_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void EPD16_patch_whenNotAutorized_then401() throws Exception {
        orderDto.setAssistance("new");
        MvcResult mvcResult = this.mockMvc.perform(patch(ORDER_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDto.toString()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void EPD17_patch_authorizedAsUser_then403() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(patch(ORDER_BASE_URI + "?id=1&status=ERLEDIGT")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void EPD18_patchTable_whenPatched_then200() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(patch(ORDER_BASE_URI + "?id=1&status=ERLEDIGT")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

}
