package com.ankurpathak.springsessiontest;

import java.util.Optional;

public interface ITokenService extends IDomainService<Token, String> {
    Optional<Token> generateToken();
    Optional<Token> byValue(String token);

}
