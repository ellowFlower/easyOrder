package at.ac.tuwien.sepm.groupphase.backend.unittests.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StatistikEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserRepository userRepository;

    private ApplicationUser applicationUser;
    private ApplicationUser user;


    @BeforeEach
    public void beforeEach() {


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
    }

    @AfterEach
    public void afterEach(){


        userRepository.delete(user);
        userRepository.delete(applicationUser);
    }

    @Test
    public void EPD1_overallTurnover_returnStatistic_then200() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/turnover/overall")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD2_overallTurnover_noStatistics_then404() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/turnover/overall")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD3_overallTurnover_unauthorized_then403() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/turnover/overall")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void EPD4_overallSales_returnSales_then200() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/sales/overall")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD5_overallSales_noSales_then404() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/sales/overall")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD6_overallSales_unauthorized_then403() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/sales/overall")
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void EPD7_timedTurnoverStat_returnStatistic_then200() throws Exception {
        String s = "?from=1111-11-10&to=2222-11-02";

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/sales" + s)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD8_timedTurnoverStat_invalidTime_then404() throws Exception {
        String s = "?from=2222-11-10&to=1111-11-02";

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/turnover" + s)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void EPD9_timedTurnoverStat_unauthorized_then403() throws Exception {
        String s = "?from=1111-11-01&to=1111-11-02";

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/turnover" + s)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void EPD10_timedSalesStat_returnSales_then200() throws Exception {
        String s = "?from=1111-11-10&to=2222-11-02";

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/sales" + s)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void EPD11_timedSalesStat_invalidTime_then401() throws Exception {
        String s = "?from=2222-11-10&to=1111-11-02";

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/sales" + s)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), ADMIN_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void EPD12_timedSalesStat_unauthorized_then403() throws Exception {
        String s = "?from=1111-11-01&to=1111-11-02";

        MvcResult mvcResult = this.mockMvc.perform(get(STATISTIC_BASE_URI + "/sales" + s)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(), USER_ROLES))
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

}
