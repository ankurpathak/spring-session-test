package com.github.ankurpathak.api.event;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.User;

public class BusinessAddedEvent extends ExtendedApplicationEvent<Business> {
    private final User user;
    public BusinessAddedEvent(Business business, User user) {
        super(business);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
