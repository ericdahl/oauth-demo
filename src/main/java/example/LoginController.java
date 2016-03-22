package example;

import example.model.App;
import example.model.User;
import example.service.AppService;
import example.service.TokenService;
import example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final TokenService tokenService;
    private final AppService appService;
    private final UserService userService;

    @Autowired
    public LoginController(TokenService tokenService, AppService appService, UserService userService) {
        this.tokenService = tokenService;
        this.appService = appService;
        this.userService = userService;
    }


    @RequestMapping(value = "/login",
                    method = RequestMethod.POST)
    public String authorize(final HttpSession httpSession,
                            @RequestParam("username") final String username,
                            @RequestParam("password") final String password) {


        final User user = userService.authenticate(username, password);

        httpSession.setAttribute("username", username);
        return "";

    }



}
