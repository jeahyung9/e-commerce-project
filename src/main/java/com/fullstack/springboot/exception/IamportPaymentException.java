package com.fullstack.springboot.exception;

public class IamportPaymentException extends RuntimeException {
    public IamportPaymentException(String message) {
        super(message);
    }

    public IamportPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
