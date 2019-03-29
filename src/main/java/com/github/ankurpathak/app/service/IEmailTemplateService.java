package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.Token;
import com.github.ankurpathak.app.User;

public interface IEmailTemplateService {
    String createAccountEnableHtml(User user, Token token);

    String createForgetPasswordHtml(User user, Token token);
}
