package org.example;

import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@SpringApplicationConfiguration(Application.class)
public class DemoTests {

    @Value("http://localhost:${local.server.port}")
    private String target;

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                // don't throw exceptions so we can more easily assert on them
                return false;
            }
        });
    }


    @Test
    public void shouldGetToken() throws Exception {
        getPasswordToken();
    }

    @Test
    public void shouldGetClientCredentialsToken() throws Exception {
        getClientCredentialsToken("myid", "mysecret");
    }

    @Test
    public void shouldGetAppByClientCredentialsToken() throws Exception {
        final String token = getClientCredentialsToken("myid", "mysecret");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final HttpEntity httpEntity = new HttpEntity(headers);
        final ResponseEntity<String> responseEntity = restTemplate.exchange(target + "/oauth/apps/me", HttpMethod.GET, httpEntity, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON_UTF8));

        final String json = responseEntity.getBody();
        assertThat(JsonPath.read(json, "$.developer.name"), is("mydeveloperusername"));
        assertThat(JsonPath.read(json, "$.client_id"), is("myid"));
    }

    @Test
    public void shouldGetTodoStats() throws Exception {
        String token = getPasswordToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final HttpEntity httpEntity = new HttpEntity(headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(target + "/go/todos", HttpMethod.GET, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
        assertThat(JsonPath.read(responseEntity.getBody(), "$.count"), is(1));
    }

    @Test
    public void shouldNotGetTodoWithoutNecessaryScope() throws Exception {
        String token = getPasswordToken("myusername", "mypassword", Collections.emptySet());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final HttpEntity httpEntity = new HttpEntity(headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(target + "/go/myusername/todos", HttpMethod.GET, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldNotGetOtherUsersTodo() throws Exception {
        String token = getPasswordToken("myusername2", "mypassword2");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final HttpEntity httpEntity = new HttpEntity(headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(target + "/go/myusername/todos", HttpMethod.GET, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldNotGetTodoWithClientCredentialsToken() throws Exception {
        String token = getClientCredentialsToken("myid", "mysecret");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final HttpEntity httpEntity = new HttpEntity(headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(target + "/go/myusername/todos", HttpMethod.GET, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldGet401ForInvalidToken() throws Exception {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer invalid");
        final HttpEntity httpEntity = new HttpEntity(headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(target + "/go/todos", HttpMethod.GET, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldGetTodosViaToken() throws Exception {

        String token = getPasswordToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        final HttpEntity httpEntity = new HttpEntity(headers);


        final ResponseEntity<String> responseEntity = restTemplate.exchange(target + "/go/myusername/todos?123", HttpMethod.GET, httpEntity, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

        assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
    }

    private String getPasswordToken() {
        return getPasswordToken("myusername", "mypassword");
    }

    private String getPasswordToken(final String username, final String password) {



        return getPasswordToken(username, password, new HashSet<>(Arrays.asList("todos:read")));
    }

    private String getPasswordToken(final String username,
                                    final String password,
                                    final Set<String> scopes) {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("username", username);
        form.add("password", password);

        for (String scope : scopes) {
            form.add("scope", scope);
        }

        final ResponseEntity<String> res = restTemplate.postForEntity(target + "/oauth/token", form, String.class);
        assertThat(res.getStatusCode(), is(HttpStatus.OK));

        final String token = JsonPath.read(res.getBody(), "$.access_token");
        assertThat(token, is(notNullValue()));
        return token;
    }

    private String getClientCredentialsToken(final String clientId, final String clientSecret) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        final ResponseEntity<String> res = restTemplate.postForEntity(target + "/oauth/token", form, String.class);
        assertThat(res.getStatusCode(), is(HttpStatus.OK));

        final String token = JsonPath.read(res.getBody(), "$.access_token");
        assertThat(token, is(notNullValue()));
        return token;
    }
}
