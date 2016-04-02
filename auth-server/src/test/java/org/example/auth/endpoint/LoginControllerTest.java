package org.example.auth.endpoint;

import org.example.auth.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public class LoginControllerTest {

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
    public void shouldLoginAndSetHeader() throws Exception {


        final MultiValueMap<String, String> formData = payload("myusername", "mypassword");
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(target + "/login", formData, String.class);

        System.err.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE), not(empty()));
        assertThat(responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE).get(0), containsString("JSESSIONID"));
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        final MultiValueMap<String, String> formData = payload("myusername", "mypassword");
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(target + "/authenticate", formData, String.class);

        System.err.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    }


    @Test
    public void should401BadPassword() throws Exception {

        final MultiValueMap<String, String> formData = payload("myusername", "invalid");
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(target + "/login", formData, String.class);

        System.err.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        // should it do this? nothing is set in session..
//        assertThat(responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE), is(empty()));
    }

    private static MultiValueMap<String, String> payload(final String username, final String password) {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", username);
        map.add("password", password);

        return map;
    }

}