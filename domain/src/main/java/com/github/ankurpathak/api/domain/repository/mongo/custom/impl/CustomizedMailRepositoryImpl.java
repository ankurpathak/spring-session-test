package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Mail;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedMailRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedMailRepositoryImpl extends AbstractCustomizedDomainRepository<Mail, String> implements CustomizedMailRepository {
    public CustomizedMailRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
