package com.github.ankurpathak.app;

import com.github.ankurpathak.app.controller.rest.dto.ApiCode;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;

public class FoundException extends RuntimeException {
    private final String entity;
    private final ApiCode code;
    private final String id;
    private final String property;
    private final DuplicateKeyException duplicateKeyException;
    private final BindingResult bindingResult;

    public FoundException(DuplicateKeyException duplicateKeyException, BindingResult bindingResult, String id , String property, String entity, ApiCode code) {
        super(duplicateKeyException.getMessage(), duplicateKeyException.getCause());
        this.duplicateKeyException = duplicateKeyException;
        this.bindingResult = bindingResult;
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

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public String getProperty() {
        return property;
    }

    public DuplicateKeyException getDuplicateKeyException() {
        return duplicateKeyException;
    }
}
