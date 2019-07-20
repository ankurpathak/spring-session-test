package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.RegistrationCompleteEvent;
import com.github.ankurpathak.api.event.util.EventUtil;
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

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;


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
                    EventUtil.sendAccountEnableToken(user, tokenService, emailService::sendForAccountEnable, log);
                    EventUtil.sendPhoneToken(user, tokenService, smsService::sendRegistrationToken, log);
                }, () -> LogUtil.logNull(log, User.class.getSimpleName()));
    }

}
