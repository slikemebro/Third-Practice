package com.ua.glebkorobov.exceptions;

public class GetFieldException extends RuntimeException{

    public GetFieldException() {
    }

    public GetFieldException(String message) {
        super(message);
    }

    public GetFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetFieldException(Throwable cause) {
        super(cause);
    }

    public GetFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
