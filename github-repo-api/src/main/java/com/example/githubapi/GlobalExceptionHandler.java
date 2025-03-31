package com.example.githubapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        // You can customize this to handle different HTTP statuses, e.g., 404, 401, etc.
        String message = ex.getResponseBodyAsString(); // Get the error message from the response body
        int statusCode = ex.getStatusCode().value();   // Get the status code (404, 401, etc.)

        // Custom error response
        ErrorResponse errorResponse = new ErrorResponse(statusCode, message);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }
}
