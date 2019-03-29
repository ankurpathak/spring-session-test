package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.Token;
import com.github.ankurpathak.app.service.IDomainService;

import java.util.Optional;

public interface ITokenService extends IDomainService<Token, String> {
    Optional<Token> generateToken();
    Optional<Token> byValue(String token);

}
