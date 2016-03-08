package example;

import example.exceptions.MissingAuthorizationHeaderException;
import example.model.App;
import example.model.Token;
import example.model.TokenResponse;
import example.service.AppService;
import example.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class OAuthController {

    private final TokenService tokenService;
    private final AppService appService;

    @Autowired
    public OAuthController(TokenService tokenService, AppService appService) {
        this.tokenService = tokenService;
        this.appService = appService;
    }

    @RequestMapping(value = "/token",
                    method = RequestMethod.POST,
                    params = {"grant_type=client_credentials", "client_id", "client_secret"})
    public TokenResponse clientCredentials(@RequestParam("client_id") String clientId) {
        return new TokenResponse(tokenService.generate(clientId));
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
