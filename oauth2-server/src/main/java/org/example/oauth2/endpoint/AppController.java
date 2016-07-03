package org.example.oauth2.endpoint;

import org.example.oauth2.exception.ErrorResponseException;
import org.example.oauth2.model.App;
import org.example.oauth2.model.AppCreationRequest;
import org.example.oauth2.model.ErrorResponse;
import org.example.oauth2.model.Token;
import org.example.oauth2.service.AppService;
import org.example.oauth2.service.AuthTokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/oauth")
@RestController
public class AppController {

    private final AuthTokenValidationService authTokenValidationService;
    private final AppService appService;

    @Autowired
    public AppController(final AuthTokenValidationService authTokenValidationService,
                         final AppService appService) {
        this.authTokenValidationService = authTokenValidationService;
        this.appService = appService;
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

    @RequestMapping(value = "/apps",
                    method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public App register(@RequestBody final AppCreationRequest appCreationRequest) {

        final App app = appService.register(appCreationRequest.getName(), appCreationRequest.getDeveloper());

        return app;
    }





}
