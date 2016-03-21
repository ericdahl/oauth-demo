package example;

import example.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
@TestPropertySource("/short-expires-in.properties")
public class ExpiredTokenTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldRejectExpiredToken() throws Exception {
        Token token = TestUtils.getPasswordToken(mockMvc, "myusername", "mypassword");

        Thread.sleep(1000);
        mockMvc.perform(get("/myusername/todos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
//                .andExpect(jsonPath("$.error", is("invalid_client"))); // TODO: is that right?
    }

}