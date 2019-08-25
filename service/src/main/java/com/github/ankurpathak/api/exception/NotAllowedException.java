package com.github.ankurpathak.api.exception;

import com.github.ankurpathak.api.rest.controller.dto.ApiCode;

public class NotAllowedException extends RuntimeException {
    private final ApiCode apiCode;

    public NotAllowedException(ApiCode apiCode) {
        this.apiCode = apiCode;
    }

    public ApiCode getApiCode() {
        return apiCode;
    }
}
