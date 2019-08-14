package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.*;
import com.github.ankurpathak.api.domain.repository.mongo.IMailRepository;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.IMailService;
import com.github.ankurpathak.api.service.dto.EmailContext;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

@Service
public class MailService extends AbstractDomainService<Mail, String> implements IMailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);


    private final IMailRepository dao;
    private final IFileService fileService;

    public MailService(IMailRepository dao, IFileService fileService) {
        super(dao);
        this.dao = dao;
        this.fileService = fileService;
    }


    @Override
    public void processEmailContext(EmailContext context, Domain<?> domain) {
        require(context, notNullValue());
        Mail mail = context.toMail();
        if(User.class.isAssignableFrom(domain.getClass())){
            mail.toUserId((BigInteger) domain.getId());
        }
        if(Customer.class.isAssignableFrom(domain.getClass())){
            mail.toCustomerId((CustomerId) domain.getId());
        }

        context.getEmailAttachments().stream().forEach(attachment -> {
            try {
                String id = fileService.store(attachment.getDataSource().getInputStream(), attachment.getOriginalFileName(), attachment.getMime(), Collections.emptyMap());
                mail.addAttachment(id);

            } catch (IOException ex) {
                LogUtil.logStackTrace(log, ex);
            }
        });
       create(mail);
    }
}
