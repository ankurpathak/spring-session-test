package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedRoleRepositoryImpl extends AbstractCustomizedDomainRepository<Role, String> implements CustomizedRoleRepository {

    public CustomizedRoleRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
