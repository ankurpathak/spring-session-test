package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.ISmsTemplateService;
import com.github.ankurpathak.api.service.dto.SmsMessages;
import org.springframework.stereotype.Service;

@Service
public class SmsTemplateService implements ISmsTemplateService {

    private final IMessageService messageService;

    public SmsTemplateService(IMessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public String createLoginTokenText(String... params) {
        return messageService.getMessage(SmsMessages.LOGIN_TOKEN, params);
    }

    @Override
    public String createRegistrationTokenText(String... params) {
        return messageService.getMessage(SmsMessages.REGISTRATION_TOKEN, params);
    }
}
