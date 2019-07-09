package com.github.ankurpathak.api.exception;

import com.github.ankurpathak.api.controller.rest.dto.ApiCode;
import org.springframework.validation.BindingResult;

public class ValidationException extends RuntimeException {
    private BindingResult bindingResult;
    private ApiCode code;
    public ValidationException(BindingResult bindingResult, String message, ApiCode code) {
        super(message);
        this.bindingResult = bindingResult;
        this.code = code;
    }


    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public ApiCode getCode() {
        return code;
    }
}
