package org.example.oauth2.endpoint;

import org.example.oauth2.exception.AuthorizationException;
import org.example.oauth2.exception.ExpiredTokenException;
import org.example.oauth2.exception.NoSuchTokenException;
import org.example.oauth2.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OAuthExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthExceptionAdvice.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleError(AuthorizationException e) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleError(ExpiredTokenException e) {
        LOGGER.info("Token [{}] is expired", e.getExpiredToken().getAccessToken());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleNoTokenError(NoSuchTokenException e) {
        LOGGER.info("No token found [{}]", e.getExpiredToken());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(ResourceNotFoundException e) {
    }
}
