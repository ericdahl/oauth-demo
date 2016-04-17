package org.example.oauth2.endpoint;

import org.example.oauth2.exception.ErrorResponseException;
import org.example.oauth2.exception.MissingAuthorizationHeaderException;
import org.example.oauth2.model.*;
import org.example.oauth2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/oauth")
@RestController
public class AppController {

    private final AuthTokenValidationService authTokenValidationService;
    private final AppService appService;
    private final UserService userService;

    @Autowired
    public AppController(final AuthTokenValidationService authTokenValidationService,
                         final AppService appService,
                         final UserService userService) {
        this.authTokenValidationService = authTokenValidationService;
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

        Token token = authTokenValidationService.validate(authorizationHeader);

        if (appId.equals("me")) {
            appId = token.getClientId();
        } else {
            throw new UnsupportedOperationException();
        }

        return appService.getById(token.getClientId());
    }



}
