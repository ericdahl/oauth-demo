package org.example.oauth2.service;

import org.example.oauth2.exception.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RemoteUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteUserService.class);

    private final String authUrl;
    private final RestTemplate restTemplate;


    @Autowired
    public RemoteUserService(@Value("${oauth2.password.auth.url}") final String authUrl) {
        this.authUrl = authUrl;
        this.restTemplate = new RestTemplate();
    }

    public void authenticate(String username, String password) {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        try {
            LOGGER.info("Authenticating [{}] with URL [{}]", username, authUrl);
            restTemplate.postForEntity(authUrl, map, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthorizationException("failed to auth", e);
            }
            throw e;
        }
    }
}
