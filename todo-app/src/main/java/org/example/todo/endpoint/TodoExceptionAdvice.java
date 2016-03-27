package org.example.todo.endpoint;

import org.example.todo.exception.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TodoExceptionAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleError(AuthorizationException e) {
    }

}
