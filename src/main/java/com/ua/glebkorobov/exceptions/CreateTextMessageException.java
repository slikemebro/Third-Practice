package com.ua.glebkorobov.exceptions;

public class CreateTextMessageException extends RuntimeException{
    public CreateTextMessageException() {
    }

    public CreateTextMessageException(String message) {
        super(message);
    }

    public CreateTextMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateTextMessageException(Throwable cause) {
        super(cause);
    }

    public CreateTextMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
