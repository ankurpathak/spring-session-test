package com.github.ankurpathak.app;

import com.github.ankurpathak.app.controller.rest.dto.ApiCode;

public class NotFoundException extends RuntimeException {
    private final String entity;
    private final ApiCode code;
    private final String id;
    private final String property;
    public NotFoundException(String id ,String property, String entity, ApiCode code) {
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
}
