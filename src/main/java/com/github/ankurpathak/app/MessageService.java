package com.github.ankurpathak.app;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

@Service
public class MessageService implements IMessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Override
    public String getMessage(String key) {
        return MessageUtil.getMessage(messageSource, key);
    }

    @Override
    public String getMessage(String key, String... args) {
        return MessageUtil.getMessage(messageSource, key, args);
    }

    @Override
    public String getMessage(FieldError error) {
        return MessageUtil.getMessage(messageSource, error);
    }
}
