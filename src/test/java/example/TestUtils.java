package example;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.Token;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ecd on 3/13/16.
 */
public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Token getPasswordToken(final MockMvc mockMvc, final String username,
                                          final String password) throws Exception {
        String response = mockMvc.perform(post("/token")
                .param("grant_type", "password")
                .param("username", username)
                .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is("bearer")))
                .andReturn().getResponse().getContentAsString();

        Token token = objectMapper.readValue(response, Token.class);

        return token;
    }
}
