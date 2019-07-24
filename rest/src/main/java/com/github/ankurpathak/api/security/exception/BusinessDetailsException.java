package com.github.ankurpathak.api.security.exception;

import org.springframework.security.core.AuthenticationException;

public class BusinessDetailsException extends AuthenticationException {
    public BusinessDetailsException(String msg) {
        super(msg);
    }
}
