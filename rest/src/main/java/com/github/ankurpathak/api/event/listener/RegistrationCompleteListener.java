package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.RegistrationCompleteEvent;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.ISmsService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.service.IUserService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Optional;

import static org.valid4j.Assertive.*;


import static org.hamcrest.Matchers.*;


@Component
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {
    private static final Logger log = LoggerFactory.getLogger(RegistrationCompleteListener.class);

    private final IUserService service;
    private final IEmailService emailService;
    private final ISmsService smsService;
    private final ITokenService tokenService;

    public RegistrationCompleteListener(IUserService service, IEmailService emailService, ISmsService smsService, ITokenService tokenService) {
        this.service = service;
        this.emailService = emailService;
        this.smsService = smsService;
        this.tokenService = tokenService;
    }


    @Override
    @Async
    public void onApplicationEvent(final @Nonnull RegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final RegistrationCompleteEvent event) {
        require(event, notNullValue());
        Optional.ofNullable(event.getSource())
                .ifPresentOrElse(user -> {
                    Optional.ofNullable(user.getEmail())
                            .ifPresentOrElse(email -> {
                                Optional.ofNullable(email.getValue())
                                        .ifPresentOrElse(emailValue -> {
                                            tokenService.findAccountToken(emailValue)
                                                    .ifPresentOrElse(token -> {
                                                        if (Instant.now().isBefore(token.getExpiry())) {
                                                            tokenService.findAccountToken(token.getValue())
                                                                    .ifPresentOrElse(reverseToken -> {
                                                                        emailService.sendForAccountEnable(user, reverseToken);
                                                                    }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                        } else {
                                                            log.info("Token: {} expired", token.getId());
                                                        }

                                                    }, () -> {
                                                        tokenService.generateAccountToken(email.getValue())
                                                                .ifPresentOrElse(token -> {
                                                                    tokenService.findAccountToken(token.getValue())
                                                                            .ifPresentOrElse(reverseToken -> {
                                                                                emailService.sendForAccountEnable(user, reverseToken);
                                                                            }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                                }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                    });
                                        }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL_VALUE, String.valueOf(user.getId())));
                            }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL, String.valueOf(user.getId())));

                    Optional.ofNullable(user.getPhone())
                            .ifPresentOrElse(phone -> {
                                Optional.ofNullable(phone.getValue())
                                        .ifPresentOrElse(phoneValue -> {
                                            tokenService.findPhoneToken(phoneValue)
                                                    .ifPresentOrElse(token -> {
                                                        if (Instant.now().isBefore(token.getExpiry())) {
                                                            smsService.sendRegistrationToken(user, token);
                                                        } else {
                                                            log.info("Token: {} expired", token.getId());
                                                        }
                                                    }, () -> {
                                                        tokenService.generatePhoneToken(phoneValue)
                                                                .ifPresentOrElse(token -> {
                                                                    smsService.sendRegistrationToken(user, token);
                                                                }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                                                    });
                                        }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE_VALUE, String.valueOf(user.getId())));
                            }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.PHONE, String.valueOf(user.getId())));
                }, () -> LogUtil.logNull(log, User.class.getSimpleName()));
    }

}
