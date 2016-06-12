package org.example.oauth2.endpoint;

import org.example.oauth2.Application;
import org.example.oauth2.exception.NoSuchTokenException;
import org.example.oauth2.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class InternalTokenControllerTest {

    private static final String TOKENS_PATH = "/internal/tokens/";
    private static final String TOKEN_PATH = "/internal/tokens/{token}";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldLoadToken() throws Exception {
        final Token token = TestUtils.getPasswordToken(mockMvc, "myusername", "mypassword");

        mockMvc.perform(get(TOKEN_PATH, token.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.token.access_token", is(token.getAccessToken())))
                .andExpect(jsonPath("$.token.expires_in", is((int) token.getExpiresIn())));
    }

    @Test
    public void shouldReturn404UnknownToken() throws Exception {
        mockMvc.perform(get(TOKEN_PATH, "unknown"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldLoadAllTokens() throws Exception {
        final Token token = TestUtils.getPasswordToken(mockMvc, "myusername", "mypassword");

        mockMvc.perform(get(TOKENS_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }


}