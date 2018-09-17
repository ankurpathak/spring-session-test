package com.ankurpathak.springsessiontest;

public interface IEmailTemplateService {
    String createAccountEnableHtml(User user, Token token);

    String createForgetPasswordHtml(User user, Token token);
}
