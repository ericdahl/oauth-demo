package org.example.todo.endpoint;

import org.example.todo.exception.AuthorizationException;
import org.example.todo.model.Todo;
import org.example.todo.model.TodoStats;
import org.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/todos")
@RestController
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(value = "/{username}/todos", method = RequestMethod.GET, headers = {"X-username"})
    public List<Todo> getTodos(@PathVariable String username,
                               @RequestHeader("X-username") final String oauthUser) {

        if (!username.equals(oauthUser)) {

            throw new AuthorizationException("wrong username");
        }


        return getTodosInternal(username);
    }

    @RequestMapping(value = "/{username}/todos", method = RequestMethod.GET)
    public List<Todo> getTodos(@PathVariable String username,
                               HttpSession httpSession) {
        final String sessionUsername = (String) httpSession.getAttribute("username");
        if (sessionUsername == null || !sessionUsername.equals(username)) {
            throw new AuthorizationException("not allowed to access resource for user");
        }
        return getTodosInternal(sessionUsername);

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public TodoStats getTodos() {
        return todoService.getStats();
    }



    private List<Todo> getTodosInternal(final String username) {
        return todoService.getTodos(username);
    }
}
