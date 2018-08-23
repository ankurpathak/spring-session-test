package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;



public class RegisterRestController extends AbstractRestController<User, UserDto, String> {
    private final IUserService service;

    @Override
    public IDomainService<User, String> getService() {
        return service;
    }

    public RegisterRestController(ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource, IUserService service) {
        super(applicationEventPublisher, messageSource);
        this.service = service;

    }








}
