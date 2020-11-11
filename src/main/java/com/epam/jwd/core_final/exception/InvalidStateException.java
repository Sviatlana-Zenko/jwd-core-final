package com.epam.jwd.core_final.exception;

public class InvalidStateException extends RuntimeException {
    // todo
    private final String fileName;

    public InvalidStateException(String fileName) {
        super();
        this.fileName = fileName;
    }

    @Override
    public String getMessage() {
        return "File '" + fileName + "' is inaccessible or does not exist.";
    }
}
