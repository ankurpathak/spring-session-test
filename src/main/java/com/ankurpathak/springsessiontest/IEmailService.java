package com.ankurpathak.springsessiontest;

import org.springframework.stereotype.Component;

@Component
public class IEmailService {
    public void sendForRegistration(User user) {
        System.out.println(user.getEmail().getToken().getValue());
    }
}
