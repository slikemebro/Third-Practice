package com.ua.glebkorobov.exceptions;

public class ParsePojoException extends RuntimeException{

    public ParsePojoException() {
    }

    public ParsePojoException(String message) {
        super(message);
    }

    public ParsePojoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsePojoException(Throwable cause) {
        super(cause);
    }

    public ParsePojoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
