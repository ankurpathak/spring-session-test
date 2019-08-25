package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTaskRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTextRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedTaskRepositoryImpl extends AbstractCustomizedDomainRepository<Task, String> implements CustomizedTaskRepository {
    public CustomizedTaskRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
