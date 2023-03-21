package com.ua.glebkorobov.exceptions;

public class CloseWriterException extends RuntimeException{

    public CloseWriterException() {
    }

    public CloseWriterException(String message) {
        super(message);
    }

    public CloseWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloseWriterException(Throwable cause) {
        super(cause);
    }

    public CloseWriterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
