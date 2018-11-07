package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedTokenRepositoryImpl extends AbstractCustomizedDomainRepository<Token, String> implements CustomizedTokenRepository {
    public CustomizedTokenRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
