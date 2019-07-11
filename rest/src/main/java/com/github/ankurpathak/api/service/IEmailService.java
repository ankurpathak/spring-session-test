package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;

public interface IEmailService {
    void sendForAccountEnable(User user, Token token, boolean async);

    void sendForForgetPassword(User user, Token token, boolean async);

    void sendTextEmail(String email, String subject, String body, String replyTo, String... ccs);
    void sendTextEmail(String email, String subject, String body);
}
