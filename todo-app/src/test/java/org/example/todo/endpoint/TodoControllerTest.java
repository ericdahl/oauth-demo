package org.example.todo.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.todo.Application;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class TodoControllerTest {

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
    public void shouldGetTodos() throws Exception {
        mockMvc.perform(get("/todos/myusername/todos")
                .header("X-username", "myusername"))
                .andExpect(status().isOk());
    }

    @Ignore
    @Test
    public void shouldRefuseToReadOtherUsersTodo() throws Exception {
        mockMvc.perform(get("/todos/otherusername/todos")
                .header("X-username", "myusername"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldShowStats() throws Exception {
        mockMvc.perform(get("/todos/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(1)));
    }


}