package org.example.todo.endpoint;

import org.example.todo.exception.AuthorizationException;
import org.example.todo.model.Todo;
import org.example.todo.model.TodoStats;
import org.example.todo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/todos")
@RestController
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(value = "/{username}/todos", method = RequestMethod.GET)
    public List<Todo> getTodos(@PathVariable String username) {
        LOGGER.info("Getting todos for user [{}]", username);

        return getTodosInternal(username);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public TodoStats getTodos() {
        return todoService.getStats();
    }



    private List<Todo> getTodosInternal(final String username) {
        return todoService.getTodos(username);
    }
}
