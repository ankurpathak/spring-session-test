package com.ankurpathak.springsessiontest;

import org.springframework.dao.DuplicateKeyException;

public class FoundException extends DuplicateKeyException {
    private final String entity;
    private final ApiCode code;
    private final String id;
    private final String property;
    private final DuplicateKeyException duplicateKeyException;
    public FoundException(DuplicateKeyException duplicateKeyException,String id ,String property, String entity, ApiCode code) {
        super(duplicateKeyException.getMessage(), duplicateKeyException.getCause());
        this.duplicateKeyException = duplicateKeyException;
        this.id = id;
        this.property = property;
        this.entity = entity;
        this.code = code;
    }
    public String getEntity() {
        return entity;
    }

    public ApiCode getCode() {
        return code;
    }

    public String getId() {
        return id;
    }


    public String getProperty() {
        return property;
    }

    public DuplicateKeyException getDuplicateKeyException() {
        return duplicateKeyException;
    }
}
