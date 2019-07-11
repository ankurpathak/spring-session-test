package com.github.ankurpathak.api.event;


import com.github.ankurpathak.api.domain.model.User;

public class RegistrationCompleteEvent extends ExtendedApplicationEvent<User> {
    private final User user;
    private final boolean async;
    public RegistrationCompleteEvent(final User user, boolean async) {
        super(user);
        this.user = user;
        this.async = async;
    }


    public User getUser() {
        return user;
    }

    public boolean isAsync() {
        return async;
    }
}

