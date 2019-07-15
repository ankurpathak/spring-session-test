package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTextRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedTextRepositoryImpl extends AbstractCustomizedDomainRepository<Text, String> implements CustomizedTextRepository {
    public CustomizedTextRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
