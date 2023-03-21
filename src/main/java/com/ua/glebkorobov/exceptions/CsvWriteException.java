package com.ua.glebkorobov.exceptions;

public class CsvWriteException extends RuntimeException{

    public CsvWriteException() {
    }

    public CsvWriteException(String message) {
        super(message);
    }

    public CsvWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsvWriteException(Throwable cause) {
        super(cause);
    }

    public CsvWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
