package org.example.oauth2.endpoint;

import org.example.oauth2.dao.TokenDAO;
import org.example.oauth2.exception.NoSuchTokenException;
import org.example.oauth2.model.internal.InternalTokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/internal/tokens")
@RestController
public class InternalTokenController {

    private final TokenDAO tokenDAO;

    @Autowired
    public InternalTokenController(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @RequestMapping("/{token}")
    public InternalTokenData getToken(@PathVariable final String token) {
        return new InternalTokenData(tokenDAO.getToken(token));
    }

    @ExceptionHandler(NoSuchTokenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchTokenException() {

    }
}