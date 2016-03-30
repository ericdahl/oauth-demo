package org.example.oauth2.endpoint;

import org.example.oauth2.exception.ErrorResponseException;
import org.example.oauth2.exception.MissingAuthorizationHeaderException;
import org.example.oauth2.model.*;
import org.example.oauth2.service.AppService;
import org.example.oauth2.service.TokenService;
import org.example.oauth2.service.RemoteUserService;
import org.example.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/oauth")
@RestController
public class AppController {

    private final TokenService tokenService;
    private final AppService appService;
    private final UserService userService;

    @Autowired
    public AppController(final TokenService tokenService,
                         final AppService appService,
                         final UserService userService) {
        this.tokenService = tokenService;
        this.appService = appService;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleErrorResponse(final ErrorResponseException e) {
        return e.getError();
    }

    @RequestMapping(value = "/apps/{appId}",
                    method = RequestMethod.GET,
                    headers = "Authorization")
    public App getApp(@PathVariable String appId,
                      @RequestHeader("Authorization") final String authorizationHeader) {

        Pattern pattern = Pattern.compile("^Bearer ([a-z0-9-]+)");
        Matcher matcher = pattern.matcher(authorizationHeader);
        if (!matcher.matches()) {
            throw new MissingAuthorizationHeaderException();
        }
        System.err.println(matcher.group(1));
        Token token = tokenService.validate(matcher.group(1));

        if (appId.equals("me")) {
            appId = token.getClientId();
        } else {
            throw new UnsupportedOperationException();
        }


        return appService.getById(token.getClientId());
    }



}
