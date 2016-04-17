package org.example.auth.service;

import org.example.auth.exception.InvalidPasswordException;
import org.example.auth.exception.NoSuchUserException;
import org.example.auth.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        users.put("myusername", new User("myusername", "mypassword"));
        users.put("myusername2", new User("myusername2", "mypassword2"));
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
