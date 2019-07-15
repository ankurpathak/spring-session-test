package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Mail;
import com.github.ankurpathak.api.domain.repository.mongo.IMailRepository;
import com.github.ankurpathak.api.service.IMailService;
import org.springframework.stereotype.Service;

@Service
public class MailService extends AbstractDomainService<Mail, String> implements IMailService {

    private final IMailRepository dao;

    public MailService(IMailRepository dao) {
        super(dao);
        this.dao = dao;
    }


}
