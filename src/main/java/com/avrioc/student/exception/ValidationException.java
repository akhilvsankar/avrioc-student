package com.avrioc.student.exception;

/**
 * Custom validation exception class
 */
public class ValidationException extends Exception {

    /**
     * parameterized constructor for creating Exception
     *
     * @param message
     */
    public ValidationException(String message) {
        super(message);
    }
}
