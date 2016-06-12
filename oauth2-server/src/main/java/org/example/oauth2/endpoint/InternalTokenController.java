package org.example.oauth2.endpoint;

import org.example.oauth2.dao.TokenDAO;
import org.example.oauth2.model.internal.InternalTokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}