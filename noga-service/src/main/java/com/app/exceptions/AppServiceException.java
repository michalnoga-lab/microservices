package com.app.exceptions;

public class AppServiceException extends RuntimeException {

    public AppServiceException(String message) {
        super(message);
    }
}