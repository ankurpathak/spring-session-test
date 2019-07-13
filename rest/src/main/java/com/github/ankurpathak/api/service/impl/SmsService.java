package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.service.ISmsService;
import com.github.ankurpathak.api.service.ISmsTemplateService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

@Service
public class SmsService implements ISmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    private final ISmsTemplateService smsTemplateService;

    public SmsService(ISmsTemplateService smsTemplateService) {
        this.smsTemplateService = smsTemplateService;
    }

    @Override
    public void sendLoginToken(User user, Token token) {
        require(user, notNullValue());
        require(token, notNullValue());
        Optional.ofNullable(token.getValue())
                .ifPresentOrElse(tokenValue -> {
                    String sms = smsTemplateService.createLoginTokenText(tokenValue);
                    sendSmsToUser(user, sms);
                }, () -> LogUtil.logFieldNull(log, Token.class.getSimpleName(), Model.Token.Field.VALUE, token.getId()));
    }
    @Override
    public void sendRegistrationToken(User user, Token token) {
        require(user, notNullValue());
        require(token, notNullValue());
        Optional.ofNullable(token.getValue())
                .ifPresentOrElse(tokenValue -> {
                    String sms = smsTemplateService.createRegistrationTokenText(tokenValue);
                    sendSmsToUser(user, sms);
                }, () -> LogUtil.logFieldNull(log, Token.class.getSimpleName(), Model.Token.Field.VALUE, token.getId()));
    }

    private void sendSmsToUser(User user, String text){
        Optional.ofNullable(user.getPhone())
                .ifPresentOrElse(phone -> {
                    Optional.ofNullable(phone.getValue())
                            .ifPresentOrElse(mobile -> {
                                log.info("Phone: {} Text: {}",mobile, text);
                            }, ()-> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE, String.valueOf(user.getId())));
                },() -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE_VALUE, String.valueOf(user.getId())));
    }
}
