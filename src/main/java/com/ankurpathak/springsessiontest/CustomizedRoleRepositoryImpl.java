package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedRoleRepositoryImpl extends AbstractDomainCustomizedRepository<Role, String> implements CustomizedRoleRepository {
    private MongoTemplate template;

    public CustomizedRoleRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }



    @Override
    public MongoTemplate getTemplate() {
        return template;
    }
}
