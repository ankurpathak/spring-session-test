package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final IUserService service;
    private final IEmailService emailService;
    private final ISmsService smsService;

    public RegistrationCompleteListener(IUserService service, IEmailService emailService, ISmsService smsService) {
        this.service = service;
        this.emailService = emailService;
        this.smsService = smsService;
    }


    @Override
    @Async
    public void onApplicationEvent(final RegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final RegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createContactVerificationToken(user, user.getEmail());
        emailService.sendForRegistration(user);
    }

}
