package org.example.oauth2.endpoint;

import org.example.oauth2.dao.AppDAO;
import org.example.oauth2.dao.TokenDAO;
import org.example.oauth2.exception.NoSuchTokenException;
import org.example.oauth2.model.App;
import org.example.oauth2.model.internal.InternalTokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/internal")
@RestController
public class InternalController {

    private final TokenDAO tokenDAO;
    private final AppDAO appDAO;

    @Autowired
    public InternalController(TokenDAO tokenDAO, AppDAO appDAO) {
        this.tokenDAO = tokenDAO;
        this.appDAO = appDAO;
    }

    @RequestMapping("/tokens/{token}")
    public InternalTokenData getToken(@PathVariable final String token) {
        return new InternalTokenData(tokenDAO.getToken(token));
    }

    @RequestMapping("/tokens")
    public Set<String> getTokens() {
        // TODO paging
        return tokenDAO.getTokens();
    }

    @RequestMapping("/clients")
    public Set<String> getClients() {
        // TODO paging
        return appDAO.getClientIds();
    }

    @RequestMapping("/clients/{clientId}")
    public App getClient(@PathVariable final String clientId) {
        return appDAO.get(clientId);
    }


    @ExceptionHandler(NoSuchTokenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchTokenException() {

    }
}