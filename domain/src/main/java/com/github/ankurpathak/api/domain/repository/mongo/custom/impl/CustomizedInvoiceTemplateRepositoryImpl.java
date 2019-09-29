package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.InvoiceTemplate;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedInvoiceTemplateRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedInvoiceTemplateRepositoryImpl extends AbstractCustomizedDomainRepository<InvoiceTemplate, String> implements CustomizedInvoiceTemplateRepository {
    public CustomizedInvoiceTemplateRepositoryImpl(MongoTemplate template) {
        super(template);
    }
}
