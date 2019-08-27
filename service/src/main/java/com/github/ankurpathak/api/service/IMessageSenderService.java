package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.repository.dto.MessageContext;

public interface IMessageSenderService {
    void send(MessageContext context);
}
