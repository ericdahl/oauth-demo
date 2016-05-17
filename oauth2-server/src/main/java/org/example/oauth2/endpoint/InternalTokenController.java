package org.example.oauth2.endpoint;

import org.example.oauth2.model.Token;
import org.example.oauth2.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal/tokens")
@RestController
public class InternalTokenController {

    private final TokenService tokenService;

    @Autowired
    public InternalTokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping("/{token}")
    public Token getToken(@PathVariable final String token) {
        return tokenService.validate(token);
    }
}