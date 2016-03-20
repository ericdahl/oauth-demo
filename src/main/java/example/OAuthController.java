package example;

import example.exceptions.ErrorResponseException;
import example.exceptions.MissingAuthorizationHeaderException;
import example.model.App;
import example.model.Token;
import example.model.TokenResponse;
import example.model.User;
import example.service.AppService;
import example.service.TokenService;
import example.service.UserService;
import model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class OAuthController {

    private final TokenService tokenService;
    private final AppService appService;
    private final UserService userService;

    @Autowired
    public OAuthController(final TokenService tokenService,
                           final AppService appService,
                           final UserService userService) {
        this.tokenService = tokenService;
        this.appService = appService;
        this.userService = userService;
    }

    @RequestMapping(value = "/token",
                    method = RequestMethod.POST,
                    params = {"grant_type=client_credentials", "client_id", "client_secret"})
    public TokenResponse clientCredentials(@RequestParam("client_id") String clientId) {
        return new TokenResponse(tokenService.generate(clientId, appService.getById(clientId).getDeveloperUsername()));
    }

    @RequestMapping(value = "/token",
            method = RequestMethod.POST,
            params = {"grant_type=password", "username", "password"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse passwordToken(@RequestParam("username") final String username,
                                       @RequestParam("password") final String password) {
        final User user = userService.authenticate(username, password);
//        return new TokenResponse(tokenService.generate(clientId));
        Token token = tokenService.generate("myid", user.getUsername());

        return new TokenResponse(token);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(value = "/token", method = RequestMethod.POST, params = {"!grant_type"})
    public ErrorResponse handleMissingParameter() {
        throw new ErrorResponseException("invalid_request");
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

        App app = appService.getById(token.getClientId());


        return app;
    }



}
