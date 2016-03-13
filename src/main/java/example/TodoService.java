package example;

import example.model.User;
import example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ecd on 3/13/16.
 */
@Service
public class TodoService {
    private Map<User, List<Todo>> userTodos = new HashMap<User, List<Todo>>();

    private final UserService userService;

    @Autowired
    public TodoService(UserService userService) {
        this.userService = userService;

        User myuser = userService.getUser("myusername");
        userTodos.put(myuser, Arrays.asList(new Todo("first todo")));
    }

    public List<Todo> getTodos(User user) {
        return userTodos.get(user);
    }
}
