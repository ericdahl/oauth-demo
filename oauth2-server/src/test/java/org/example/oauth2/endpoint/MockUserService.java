package org.example.oauth2.endpoint;

import org.example.oauth2.exception.AuthorizationException;
import org.example.oauth2.service.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Primary
@Service
public class MockUserService implements UserService {


    public void authenticate(String username, String password) {
        if (username.equals("myusername")) {
            if (password.equals("mypassword")) {
                return;
            }
        }
        throw new AuthorizationException("invalid username/password");
    }
}
