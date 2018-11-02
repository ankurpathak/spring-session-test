package com.ankurpathak.springsessiontest;

public class InvalidException extends RuntimeException {
    private final ApiCode code;
    private final String property;
    private final String value;


    public InvalidException(ApiCode code, String property, String value) {
        this.code = code;
        this.property = property;
        this.value = value;
    }

    public ApiCode getCode() {
        return code;
    }


    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
}
