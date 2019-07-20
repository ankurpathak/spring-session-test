package com.github.ankurpathak.api.event;

import com.github.ankurpathak.api.domain.model.User;

public class LoginTokenEvent extends ExtendedApplicationEvent<User>{
    public LoginTokenEvent(User user) {
        super(user);

    }

}
