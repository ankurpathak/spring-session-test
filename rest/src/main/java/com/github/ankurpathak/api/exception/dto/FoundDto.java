package com.github.ankurpathak.api.exception.dto;

import com.github.ankurpathak.api.rest.controller.dto.ApiCode;

public class FoundDto {
    private final String entity;
    private final ApiCode code;
    private final String id;
    private final String property;

    public FoundDto(String entity, String property, String id, ApiCode code) {
        this.entity = entity;
        this.code = code;
        this.id = id;
        this.property = property;
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
