package example.service;

import example.exceptions.InvalidPasswordException;
import example.exceptions.NoSuchUserException;
import example.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ecd on 3/13/16.
 */
@Service
public class UserService {

    private final Map<String, User> users = new HashMap<String, User>();

    public UserService() {
        users.put("myusername", new User("myusername", "mypassword"));
    }

    public User authenticate(String username, String password) {
        final User user = users.get(username);
        if (user == null) {
            throw new NoSuchUserException();
        }

        if (password.equals(user.getPassword())) {
            return user;
        } else {
            throw new InvalidPasswordException();
        }

    }


    public User getUser(String username) {
        return users.get(username);
    }
}
