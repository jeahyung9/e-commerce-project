package com.fullstack.springboot.exception;

public class IamportTokenException extends RuntimeException {
    public IamportTokenException(String message) {
        super(message);
    }

    public IamportTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
