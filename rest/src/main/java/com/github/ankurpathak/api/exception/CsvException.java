package com.github.ankurpathak.api.exception;

public class CsvException extends RuntimeException {
    private final String file;
    public CsvException(String message, Throwable cause, String file) {
        super(message, cause);
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
