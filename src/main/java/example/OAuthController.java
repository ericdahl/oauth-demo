package example;

import example.exceptions.MissingAuthorizationHeaderException;
import example.model.Token;
import example.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class OAuthController {

    private final TokenService tokenService;

    @Autowired
    public OAuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/token",
                    method = RequestMethod.POST,
                    params = {"grant_type=client_credentials", "client_id", "client_secret"})
    public Token clientCredentials() {
        return tokenService.generate();
    }

    @RequestMapping(value = "/apps/{appId}",
                    method = RequestMethod.GET,
                    headers = "Authorization")
    public App getApp(@PathVariable final String appId,
                      @RequestHeader("Authorization") final String authorizationHeader) {

        Pattern pattern = Pattern.compile("^Bearer ([a-z0-9-]+)");
        Matcher matcher = pattern.matcher(authorizationHeader);
        if (!matcher.matches()) {
            throw new MissingAuthorizationHeaderException();
        }
        System.err.println(matcher.group(1));
        Token token = tokenService.validate(matcher.group(1));


        return null;
    }
}
