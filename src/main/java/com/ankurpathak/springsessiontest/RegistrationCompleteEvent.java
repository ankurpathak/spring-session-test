package com.ankurpathak.springsessiontest;


public class RegistrationCompleteEvent extends ExtendedApplicationEvent<User> {
    private final User user;
    public RegistrationCompleteEvent(final User user) {
        super(user);
        this.user = user;
    }


    public User getUser() {
        return user;
    }
}

