package com.github.ankurpathak.api.exception;

public class TooManyException extends RuntimeException {
    Object id;

    public TooManyException(Object id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }
}
