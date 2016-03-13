package example;

import example.model.User;
import example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ecd on 3/13/16.
 */
@RestController
public class TodoController {

    private final UserService userService;
    private final TodoService todoService;

    @Autowired
    public TodoController(UserService userService, TodoService todoService) {
        this.userService = userService;
        this.todoService = todoService;
    }

    @RequestMapping(value = "/{username}/todos", method = RequestMethod.GET)
    public List<Todo> getTodos(@PathVariable String username) {

        User user = userService.getUser(username);

        return todoService.getTodos(user);

    }
}
