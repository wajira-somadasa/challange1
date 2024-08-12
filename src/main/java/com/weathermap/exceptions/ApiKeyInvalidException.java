package com.weathermap.exceptions;

public class ApiKeyInvalidException extends RuntimeException {
    public ApiKeyInvalidException(String message) {
        super(message);
    }
}