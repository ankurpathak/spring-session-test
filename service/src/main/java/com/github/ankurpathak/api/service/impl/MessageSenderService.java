package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.IMessageSenderRepository;
import com.github.ankurpathak.api.domain.repository.dto.MessageContext;
import com.github.ankurpathak.api.service.IMessageSenderService;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService implements IMessageSenderService {
    private final IMessageSenderRepository messageSenderRepository;

    public MessageSenderService(IMessageSenderRepository messageSenderRepository) {
        this.messageSenderRepository = messageSenderRepository;
    }

    @Override
    public void send(MessageContext context) {
        messageSenderRepository.send(context);
    }
}
