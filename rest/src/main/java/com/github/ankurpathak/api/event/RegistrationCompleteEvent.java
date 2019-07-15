package com.github.ankurpathak.api.event;


import com.github.ankurpathak.api.domain.model.User;

public class RegistrationCompleteEvent extends ExtendedApplicationEvent<User> {
    public RegistrationCompleteEvent(final User user) {
        super(user);

    }
}

