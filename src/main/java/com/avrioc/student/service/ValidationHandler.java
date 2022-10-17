package com.avrioc.student.service;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller advice class used for sending exception/validation messages
 * Bean validation exceptions will be caught and send as response back to client.
 */
@ControllerAdvice
public class ValidationHandler {

    /**
     * exception handler method invoked for exceptions and errors after returning from a controller.
     *
     * @param e
     * @return Collection of String wrapped using response entity
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<String>> handleException(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

}