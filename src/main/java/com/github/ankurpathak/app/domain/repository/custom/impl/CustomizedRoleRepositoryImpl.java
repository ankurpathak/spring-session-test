package com.github.ankurpathak.app.domain.repository.custom.impl;

import com.github.ankurpathak.app.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.app.Role;
import com.github.ankurpathak.app.domain.repository.custom.CustomizedRoleRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedRoleRepositoryImpl extends AbstractCustomizedDomainRepository<Role, String> implements CustomizedRoleRepository {

    public CustomizedRoleRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
