package example;

import example.exceptions.AuthorizationException;
import example.exceptions.MissingAuthorizationHeaderException;
import example.model.Token;
import example.model.User;
import example.service.TokenService;
import example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ecd on 3/13/16.
 */
@RestController
public class TodoController {

    private final UserService userService;
    private final TodoService todoService;
    private final TokenService tokenService;

    @Autowired
    public TodoController(UserService userService, TodoService todoService, TokenService tokenService) {
        this.userService = userService;
        this.todoService = todoService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/{username}/todos", method = RequestMethod.GET, headers = {"Authorization"})
    public List<Todo> getTodos(@PathVariable String username,
                               @RequestHeader("Authorization") final String authorizationHeader) {

        Pattern pattern = Pattern.compile("^Bearer ([a-z0-9-]+)");
        Matcher matcher = pattern.matcher(authorizationHeader);
        if (!matcher.matches()) {
            throw new MissingAuthorizationHeaderException();
        }
        System.err.println(matcher.group(1));
        Token token = tokenService.validate(matcher.group(1));
        userService.getUser(token.getUsername());
        if (!username.equals(token.getUsername())) {
            throw new AuthorizationException("wrong username");
        }


        return getTodosInternal(username);
    }

    @RequestMapping(value = "/{username}/todos", method = RequestMethod.GET)
    public List<Todo> getTodos(@PathVariable String username,
                               HttpSession httpSession) {
        final String sessionUsername = (String) httpSession.getAttribute("username");
        if (sessionUsername == null || !sessionUsername.equals(username)) {
            throw new AuthorizationException("not allowed to access resouce for user");
        }
        return getTodosInternal(sessionUsername);

    }

    public List<Todo> getTodosInternal(final String username) {
        User user = userService.getUser(username);

        return todoService.getTodos(user);
    }
}
