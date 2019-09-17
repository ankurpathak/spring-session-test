package com.github.ankurpathak.api.exception;

import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.util.MatcherUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.require;

public class ValidationException extends RuntimeException {
    private List<BindingResult> bindingResults;
    private ApiCode code;
    private String[] messages;
    public ValidationException(List<BindingResult> bindingResults, ApiCode code, String...messages) {
        require(bindingResults, MatcherUtil.notCollectionEmpty());
        require(code, notNullValue());
        require(messages, not(emptyArray()));
        this.bindingResults = bindingResults;
        this.code = code;
        this.messages = messages;
    }

    public ValidationException(List<BindingResult> bindingResults) {
        require(bindingResults, MatcherUtil.notCollectionEmpty());
        this.bindingResults = bindingResults;
        this.code = ApiCode.VALIDATION;
        this.messages = ArrayUtils.EMPTY_STRING_ARRAY;
    }


    public List<BindingResult> getBindingResults() {
        return bindingResults;
    }

    public ApiCode getCode() {
        return code;
    }

    public String[] getMessages() {
        return messages;
    }
}
