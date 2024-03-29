package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedRoleRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedRoleRepositoryImpl extends AbstractCustomizedDomainRepository<Role, String> implements CustomizedRoleRepository {
    public CustomizedRoleRepositoryImpl(MongoTemplate template) {
        super(template);
    }
}
