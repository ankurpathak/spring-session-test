package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Mail;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.service.dto.EmailContext;

public interface IMailService extends IDomainService<Mail, String> {
    void processEmailContext(EmailContext context, Domain<?> to);
}
