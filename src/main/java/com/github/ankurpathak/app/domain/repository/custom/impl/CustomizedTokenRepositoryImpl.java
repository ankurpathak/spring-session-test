package com.github.ankurpathak.app.domain.repository.custom.impl;

import com.github.ankurpathak.app.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.app.Token;
import com.github.ankurpathak.app.domain.repository.custom.CustomizedTokenRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedTokenRepositoryImpl extends AbstractCustomizedDomainRepository<Token, String> implements CustomizedTokenRepository {
    public CustomizedTokenRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
