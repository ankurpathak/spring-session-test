package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Notification;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedNotificationRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedNotificationRepositoryImpl extends AbstractCustomizedDomainRepository<Notification, String> implements CustomizedNotificationRepository {
    public CustomizedNotificationRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
