package example;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class OAuthControllerTest {

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
    public void shouldGetToken() throws Exception {
        getToken("myid", "mysecret", "client_credentials");
    }

    @Test
    public void shouldLoadAppWithToken() throws Exception {
        Token token = getToken("myid", "mysecret", "client_credentials");

        mockMvc.perform(get("/apps/me")
                    .header("Authorization", "Bearer " + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("myapp")));
    }

    @Test
    public void shouldGetPasswordToken() throws Exception {
        TestUtils.getPasswordToken(mockMvc, "myusername", "mypassword");
    }

    @Test
    public void shouldErrorOnBadPassword() throws Exception {

        mockMvc.perform(post("/token")
                .param("grant_type", "password")
                .param("username", "myusername")
                .param("password", "invalid"))
                .andExpect(status().isUnauthorized());
    }

    private Token getToken(final String clientId,
                           final String clientSecret,
                           final String grantType) throws Exception {
        String response = mockMvc.perform(post("/token")
                .param("grant_type", grantType)
                .param("client_id", clientId)
                .param("client_secret", clientSecret))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andReturn().getResponse().getContentAsString();

        Token token = objectMapper.readValue(response, Token.class);

        return token;
    }

    @Test
    public void shouldReturnErrorIfMissingParameters() throws Exception {
        mockMvc.perform(post("/token")
//                .param("grant_type", grantType)
                .param("client_id", "myclient")
                .param("client_secret", "mysecret"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("invalid_request")));


    }

}