package example;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ecd on 3/13/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class TodoControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldRejectRequestWithoutAccessToken() throws Exception {
        mockMvc.perform(get("/myusername/todos"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAcceptRequestWithAccessToken() throws Exception {
        Token token = TestUtils.getPasswordToken(mockMvc, "myusername", "mypassword");

        mockMvc.perform(get("/myusername/todos")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}