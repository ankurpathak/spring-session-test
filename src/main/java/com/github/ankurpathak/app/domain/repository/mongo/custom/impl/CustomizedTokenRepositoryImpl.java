package com.github.ankurpathak.app.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.app.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.app.Token;
import com.github.ankurpathak.app.domain.repository.mongo.custom.CustomizedTokenRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedTokenRepositoryImpl extends AbstractCustomizedDomainRepository<Token, String> implements CustomizedTokenRepository {
    public CustomizedTokenRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
