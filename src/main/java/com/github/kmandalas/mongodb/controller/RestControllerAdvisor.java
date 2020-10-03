package com.github.kmandalas.mongodb.controller;

import com.github.kmandalas.mongodb.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedList;


@RestControllerAdvice
@Slf4j
public class RestControllerAdvisor {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError handleResourceNotFound(final Exception e) {
        log.debug("Error {}", e.getMessage(), e);
        return createError(e.getMessage(), HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError handleGenericError(final Exception e) {
        log.debug("Error {}", e.getMessage(), e);
        return createError(e.getMessage(), HttpStatus.BAD_REQUEST, e);
    }

    private RestError createError(String message, HttpStatus status, Exception e) {
        final RestError error = new RestError();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setType(status.getReasonPhrase());
        error.setException(e.getClass().getName());
        error.setErrors(new LinkedList<>());
        error.setMessage(message);
        return error;
    }
}
