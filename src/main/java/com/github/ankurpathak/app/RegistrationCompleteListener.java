package com.github.ankurpathak.app;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        require(event, notNullValue());
        tokenService.generateToken()
                .ifPresent(token -> {
                    Optional.ofNullable(event.getUser())
                            .ifPresent(user -> {
                                Optional.ofNullable(user.getEmail())
                                        .ifPresent(email -> {
                                            email.setTokenId(token.getId());
                                            service.update(user);
                                            emailService.sendForAccountEnable(user, token);
                                        });
                            });

                });
    }

}
