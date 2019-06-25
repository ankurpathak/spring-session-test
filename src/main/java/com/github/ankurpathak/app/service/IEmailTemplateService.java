package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.domain.model.Token;
import com.github.ankurpathak.app.domain.model.User;

public interface IEmailTemplateService {
    String createAccountEnableHtml(User user, Token token);

    String createForgetPasswordHtml(User user, Token token);
}
