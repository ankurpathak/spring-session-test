package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;

import java.util.Optional;

public interface ITokenService extends IDomainService<Token, String> {
    Optional<Token> generateToken();
    Optional<Token> byValue(String token);

}
