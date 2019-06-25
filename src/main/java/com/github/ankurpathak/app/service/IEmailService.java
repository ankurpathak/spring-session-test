package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.domain.model.Token;
import com.github.ankurpathak.app.domain.model.User;

public interface IEmailService {
    void sendForAccountEnable(User user, Token token);

    void sendForForgetPassword(User user, Token token);
}
