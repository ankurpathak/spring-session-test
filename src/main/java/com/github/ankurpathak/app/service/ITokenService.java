package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.domain.model.Token;

import java.util.Optional;

public interface ITokenService extends IDomainService<Token, String> {
    Optional<Token> generateToken();
    Optional<Token> byValue(String token);

}
