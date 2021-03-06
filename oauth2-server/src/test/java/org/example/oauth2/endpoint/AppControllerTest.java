package org.example.oauth2.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.example.oauth2.Application;
import org.example.oauth2.model.AppCreationRequest;
import org.example.oauth2.model.Developer;
import org.example.oauth2.model.Token;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class AppControllerTest {

    private static final String APP_PATH = "/oauth/apps/{app_id}";

    private static final Set<String> BASIC_SCOPES;
    static {
        Set<String> scopes = new HashSet<>();
        scopes.add("todos:read");
        BASIC_SCOPES = Collections.unmodifiableSet(scopes);
    }

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
    public void shouldLoadMeApp() throws Exception {
        final Token token = TestUtils.getClientCredentialsToken(mockMvc, "myid", "mysecret");

        mockMvc.perform(get(APP_PATH, "me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("myapp")));
    }

    @Test
    public void shouldLoadAppById() throws Exception {
        final Token token = TestUtils.getClientCredentialsToken(mockMvc, "myid", "mysecret");

        mockMvc.perform(get(APP_PATH, "myid")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("myapp")));
    }

    @Test
    public void shouldRegisterApp() throws Exception {
        final String response = TestUtils.registerApp(mockMvc, "mycustomapp", BASIC_SCOPES, "mydevname");

        final String clientId = JsonPath.read(response, "$.client_id");
        final String clientSecret = JsonPath.read(response, "$.client_secret");

        final Token token = TestUtils.getClientCredentialsToken(mockMvc, clientId, clientSecret);
        mockMvc.perform(get(APP_PATH, "me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("mycustomapp")));
    }

    @Test
    public void shouldNotAllowAccessToOtherApp() throws Exception {
        final String json = TestUtils.registerApp(mockMvc, "mycustomapp3", BASIC_SCOPES, "mydevname");
        final Token token = TestUtils.getClientCredentialsToken(mockMvc, "myid", "mysecret");

        mockMvc.perform(get(APP_PATH, (String) JsonPath.read(json, "$.client_id"))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotAllowDuplicateApps() throws Exception {
        TestUtils.registerApp(mockMvc, "mycustomapp1", BASIC_SCOPES, "mydevname1");

        final AppCreationRequest request = new AppCreationRequest("mycustomapp1", BASIC_SCOPES, new Developer("mydevname1"));

        mockMvc.perform(post("/oauth/apps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldRequireScopes() throws Exception {
        mockMvc.perform(post("/oauth/apps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"mycustomapp1\",\n" +
                        "    \"developer\": {\n" +
                        "        \"name\": \"mydevname2\"\n" +
                        "    }\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


}