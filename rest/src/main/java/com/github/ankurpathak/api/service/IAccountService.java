package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;

import javax.annotation.Nonnull;

public interface IAccountService {
    void accountEnableEmail(@Nonnull String email, boolean async);

    Token.TokenStatus accountEnable(@Nonnull String token);
}
