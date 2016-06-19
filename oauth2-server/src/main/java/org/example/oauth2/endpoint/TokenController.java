package org.example.oauth2.endpoint;

import org.apache.tomcat.util.codec.binary.Base64;
import org.example.oauth2.exception.AuthorizationException;
import org.example.oauth2.exception.BadRequestException;
import org.example.oauth2.exception.ErrorResponseException;
import org.example.oauth2.model.*;
import org.example.oauth2.service.AppService;
import org.example.oauth2.service.TokenService;
import org.example.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/token",
            method = RequestMethod.POST,
            params = {"grant_type=password"},
            headers = {HttpHeaders.AUTHORIZATION},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse passwordTokenWithAuthHeader(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (!authorizationHeader.startsWith("Basic ")) {
            throw new BadRequestException("Not valid authorization header");
        }
        authorizationHeader = authorizationHeader.replaceFirst("^Basic ", "");
        final byte[] bytes = Base64.decodeBase64(authorizationHeader);
        final String[] split = new String(bytes).split(":");
        if (split.length != 2) {
            throw new BadRequestException("Not a valid authorization header");
        }
        final String username = split[0];
        final String password = split[1];

        userService.authenticate(username, password);
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
