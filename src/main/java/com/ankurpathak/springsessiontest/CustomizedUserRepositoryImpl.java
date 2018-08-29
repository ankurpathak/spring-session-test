package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    private MongoTemplate mongoTemplate;

    public CustomizedUserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Page<User> search(String field, String value, Pageable pageable) {
        return null;
    }
}
