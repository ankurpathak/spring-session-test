package com.github.ankurpathak.api.exception.dto;

import com.github.ankurpathak.api.rest.controller.dto.ApiCode;

public class FoundDto {
    private final String id;
    private final String property;

    public FoundDto( String property, String id) {
        this.id = id;
        this.property = property;
    }


    public String getId() {
        return id;
    }

    public String getProperty() {
        return property;
    }
}
