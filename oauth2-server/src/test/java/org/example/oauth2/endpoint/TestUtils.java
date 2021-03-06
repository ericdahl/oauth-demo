package org.example.oauth2.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.oauth2.model.AppCreationRequest;
import org.example.oauth2.model.Developer;
import org.example.oauth2.model.Token;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Token getPasswordToken(final MockMvc mockMvc, final String username,
                                         final String password) throws Exception {
        String response = mockMvc.perform(post("/oauth/token")
                .param("grant_type", "password")
                .param("username", username)
                .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(0))))
                .andExpect(jsonPath("$.token_type", is("bearer")))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, Token.class);
    }

    public static Token getClientCredentialsToken(final MockMvc mockMvc, final String clientId,
                                         final String clientSecret) throws Exception {
        String response = mockMvc.perform(post("/oauth/token")
                .param("grant_type", "client_credentials")
                .param("client_id", clientId)
                .param("client_secret", clientSecret))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(0))))
                .andExpect(jsonPath("$.token_type", is("bearer")))
                .andReturn().getResponse().getContentAsString();

        Token token = objectMapper.readValue(response, Token.class);

        return token;
    }

    public static String registerApp(final MockMvc mockMvc,
                                     final String appName,
                                     final Set<String> scopes,
                                     final String developerUsername) throws Exception {


        final AppCreationRequest request = new AppCreationRequest(appName, scopes, new Developer(developerUsername));

        final String response = mockMvc.perform(post("/oauth/apps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.client_id", is(notNullValue())))
                .andReturn().getResponse().getContentAsString();
        return response;
    }

}
