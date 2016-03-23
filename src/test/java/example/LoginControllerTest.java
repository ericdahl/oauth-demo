package example;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public class LoginControllerTest {

//    @Autowired
//    private WebApplicationContext webApplicationContext;

//    @Autowired
//    private ObjectMapper objectMapper;

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