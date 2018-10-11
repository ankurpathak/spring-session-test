package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedTokenRepositoryImpl extends AbstractCustomizedDomainRepository<Token, String> implements CustomizedTokenRepository {
    private MongoTemplate template;

    public CustomizedTokenRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }



    @Override
    public MongoTemplate getTemplate() {
        return template;
    }
}
