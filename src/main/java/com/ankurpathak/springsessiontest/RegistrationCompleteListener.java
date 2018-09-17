package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.valid4j.Assertive.*;


import static org.hamcrest.Matchers.*;


@Component
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {
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
    public void onApplicationEvent(final RegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final RegistrationCompleteEvent event) {
        ensure(event, notNullValue());
        ensure(event.getUser(), notNullValue());
        ensure(event.getUser().getEmail(), notNullValue());
        Token token = tokenService.generateToken();
        event.getUser().getEmail().setTokenId(token.getId());
        service.update(event.getUser());
        emailService.sendForAccountEnable(event.getUser(), token);
    }

}
