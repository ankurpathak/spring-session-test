package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;

public interface IAccountService {
    void accountEnableEmail(String email);

    Token.TokenStatus accountEnable(String token);
}
