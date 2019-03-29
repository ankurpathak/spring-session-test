package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.Token;
import com.github.ankurpathak.app.User;

public interface IEmailService {
    void sendForAccountEnable(User user, Token token);

    void sendForForgetPassword(User user, Token token);
}
