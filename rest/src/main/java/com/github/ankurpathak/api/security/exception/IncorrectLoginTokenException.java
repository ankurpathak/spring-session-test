package com.github.ankurpathak.api.security.exception;

import com.github.ankurpathak.api.domain.model.Token;
import org.springframework.security.authentication.BadCredentialsException;

public class IncorrectLoginTokenException extends BadCredentialsException {
    private final Token.TokenStatus status;
    public IncorrectLoginTokenException(Token.TokenStatus status, String msg) {
        super(msg);
        this.status = status;
    }

}
