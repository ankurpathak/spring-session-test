package com.github.ankurpathak.api.domain.repository;

import com.github.ankurpathak.api.domain.repository.dto.MessageContext;

public interface IMessageSenderRepository {
    void send(MessageContext context);
}
