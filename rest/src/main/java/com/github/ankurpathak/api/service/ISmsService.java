package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;

public interface ISmsService {
    void sendLoginToken(User use, Token token);
    void sendRegistrationToken(User use, Token token);
}
