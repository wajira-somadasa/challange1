package com.weathermap.controller;

import com.weathermap.exceptions.ApiKeyInvalidException;
import com.weathermap.exceptions.WeatherNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiKeyInvalidException.class)
    public ResponseEntity<String> handleApiKeyInvalidException(ApiKeyInvalidException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(WeatherNotFoundException.class)
    public ResponseEntity<String> handleWeatherNotFoundException(WeatherNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // General exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}