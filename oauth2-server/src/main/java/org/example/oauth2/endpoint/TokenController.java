package org.example.oauth2.endpoint;

import org.example.oauth2.exception.ErrorResponseException;
import org.example.oauth2.exception.MissingAuthorizationHeaderException;
import org.example.oauth2.model.*;
import org.example.oauth2.service.AppService;
import org.example.oauth2.service.TokenService;
import org.example.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/oauth")
@RestController
public class TokenController {

    private final TokenService tokenService;
    private final AppService appService;
    private final UserService userService;

    @Autowired
    public TokenController(final TokenService tokenService,
                           final AppService appService,
                           final UserService userService) {
        this.tokenService = tokenService;
        this.appService = appService;
        this.userService = userService;
    }

    @RequestMapping(value = "/token",
                    method = RequestMethod.POST,
                    params = {"grant_type=client_credentials", "client_id", "client_secret"})
    public TokenResponse clientCredentials(@RequestParam("client_id") String clientId,
                                           @RequestParam("client_secret") String clientSecret) {
        final App app = appService.authenticate(clientId, clientSecret);
        return new TokenResponse(tokenService.generate(clientId, app.getDeveloperUsername()));
    }

    @RequestMapping(value = "/token",
            method = RequestMethod.POST,
            params = {"grant_type=password", "username", "password"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse passwordToken(@RequestParam("username") final String username,
                                       @RequestParam("password") final String password) {
        userService.authenticate(username, password);
//        return new TokenResponse(tokenService.generate(clientId));
        Token token = tokenService.generate("myid", username);

        return new TokenResponse(token);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(value = "/token", method = RequestMethod.POST, params = {"!grant_type"})
    public ErrorResponse handleMissingParameter() {

        throw new ErrorResponseException(ErrorCode.INVALID_REQUEST, "Missing mandatory fields [grant_type]");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleErrorResponse(final ErrorResponseException e) {
        return e.getError();
    }

}
