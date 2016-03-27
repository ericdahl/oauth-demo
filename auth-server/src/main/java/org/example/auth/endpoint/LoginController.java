package org.example.auth.endpoint;

import org.example.auth.model.User;
import org.example.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @ResponseBody
    @RequestMapping(value = "/login",
                    method = RequestMethod.POST)
    public void authorize(final HttpSession httpSession,
                            @RequestParam("username") final String username,
                            @RequestParam("password") final String password) {

        final User user = userService.authenticate(username, password);
        System.err.println("setting session variable");
        httpSession.setAttribute("username", username);
    }

    @ResponseBody
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.POST)
    public void authenticate(@RequestParam("username") final String username,
                             @RequestParam("password") final String password) {
        userService.authenticate(username, password);
    }
}
