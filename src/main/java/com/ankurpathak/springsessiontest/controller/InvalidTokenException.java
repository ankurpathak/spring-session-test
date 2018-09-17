package com.ankurpathak.springsessiontest.controller;

import com.ankurpathak.springsessiontest.ApiCode;

public class InvalidTokenException extends RuntimeException {
    private ApiCode code;


    public InvalidTokenException(String message, ApiCode code) {
        super(message);
        this.code = code;
    }

    public ApiCode getCode() {
        return code;
    }


}
