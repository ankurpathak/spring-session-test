package com.github.ankurpathak.api.event;

import com.github.ankurpathak.api.domain.model.User;

public class SendLoginTokenEvent extends ExtendedApplicationEvent<User>{

    public SendLoginTokenEvent(User user) {
        super(user);

    }

}
