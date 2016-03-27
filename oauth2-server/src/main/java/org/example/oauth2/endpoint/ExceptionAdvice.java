package org.example.oauth2.endpoint;

import org.example.oauth2.exception.AuthorizationException;
import org.example.oauth2.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleError(AuthorizationException e) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(ResourceNotFoundException e) {
    }
}
