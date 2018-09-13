package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class CustomizedUserRepositoryImpl extends AbstractDomainCustomizedRepository<User, BigInteger> implements CustomizedUserRepository {
    private MongoTemplate template;

    public CustomizedUserRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }



    @Override
    public MongoTemplate getTemplate() {
        return template;
    }
}
