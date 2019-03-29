package com.github.ankurpathak.app;


import com.github.ankurpathak.app.event.ExtendedApplicationEvent;

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

