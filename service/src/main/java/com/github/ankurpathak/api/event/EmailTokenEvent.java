package com.github.ankurpathak.api.event;

import com.github.ankurpathak.api.domain.model.User;

public class EmailTokenEvent extends ExtendedApplicationEvent<User>{
    public EmailTokenEvent(User user) {
        super(user);
    }
}
