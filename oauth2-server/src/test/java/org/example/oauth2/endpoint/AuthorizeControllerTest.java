package org.example.oauth2.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.oauth2.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
public class AuthorizeControllerTest {

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
    public void shouldGetAuthorizationGrant() throws Exception {
        String response = mockMvc.perform(get("/oauth/authorize")
                .param("response_type", "code")
                .param("client_id", "myid")
                .param("state", "1234"))
                .andDo(print())
                .andExpect(status().isFound())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void shouldRejectUnknownClientId() throws Exception {
        String response = mockMvc.perform(get("/authorize")
                .param("response_type", "code")
                .param("client_id", "invalid")
                .param("state", "1234"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void shouldReturnAuthScreenIfLoggedIn() throws Exception {
        // TODO
//        String response = mockMvc.perform(post("/login")
//                    .param("username", "myusername")
//                    .param("password", "mypassword"))
//                .andExpect("")
//                .param("response_type", "code")
//                .param("client_id", "invalid")
//                .param("state", "1234"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andReturn().getResponse().getContentAsString();
//
//
//        String response = mockMvc.perform(get("/authorize")
//                .param("response_type", "code")
//                .param("client_id", "invalid")
//                .param("state", "1234"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andReturn().getResponse().getContentAsString();
    }
}