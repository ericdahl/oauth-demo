package org.example.oauth2.endpoint;

import org.example.oauth2.model.App;
import org.example.oauth2.service.AppService;
import org.example.oauth2.service.TokenService;
import org.example.oauth2.service.RemoteUserService;
import org.example.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class AuthorizeController {

    private final TokenService tokenService;
    private final AppService appService;
    private final UserService userService;

    @Autowired
    public AuthorizeController(TokenService tokenService, AppService appService, UserService userService) {
        this.tokenService = tokenService;
        this.appService = appService;
        this.userService = userService;
    }


    @RequestMapping(value = "/authorize",
                    method = RequestMethod.GET,
                    params = {"response_type=code", "client_id", "state"})
    public String authorize(final HttpSession httpSession,
                            @RequestParam("client_id") final String clientId) {

        final App clientApp = appService.getById(clientId);


        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            return null;
        }

        return "redirect:/login";

    }



}
