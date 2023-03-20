package com.ua.glebkorobov.exceptions;

public class WriterCreateException extends RuntimeException{
    public WriterCreateException() {
    }

    public WriterCreateException(String message) {
        super(message);
    }

    public WriterCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriterCreateException(Throwable cause) {
        super(cause);
    }

    public WriterCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
