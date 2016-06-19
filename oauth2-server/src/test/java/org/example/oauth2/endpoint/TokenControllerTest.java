package org.example.oauth2.endpoint;

import org.example.oauth2.Application;
import org.example.oauth2.model.Token;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class TokenControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetToken() throws Exception {
        TestUtils.getClientCredentialsToken(mockMvc, "myid", "mysecret");
    }

    @Test
    public void shouldLoadAppWithToken() throws Exception {
        final Token token = TestUtils.getClientCredentialsToken(mockMvc, "myid", "mysecret");

        mockMvc.perform(get("/oauth/apps/me")
                .header("Authorization", "Bearer " + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("myapp")));
    }

    @Test
    public void shouldVerifyClientSecret() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .param("grant_type", "client_credentials")
                .param("client_id", "myid")
                .param("client_secret", "invalid"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldGetPasswordToken() throws Exception {
        TestUtils.getPasswordToken(mockMvc, "myusername", "mypassword");
    }

    @Test
    public void shouldErrorOnBadPassword() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .param("grant_type", "password")
                .param("username", "myusername")
                .param("password", "invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnErrorIfMissingParameters() throws Exception {
        mockMvc.perform(post("/oauth/token")
                .param("client_id", "myclient")
                .param("client_secret", "mysecret"))
                // no grant type
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("invalid_request")))
                .andExpect(jsonPath("$.error_description", is("Missing mandatory fields [grant_type]")));
    }

}