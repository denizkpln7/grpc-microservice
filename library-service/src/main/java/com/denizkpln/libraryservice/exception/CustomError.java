package com.denizkpln.libraryservice.exception;

public class CustomError extends RuntimeException{
    public CustomError(String message) {
        super(message);
    }
}
