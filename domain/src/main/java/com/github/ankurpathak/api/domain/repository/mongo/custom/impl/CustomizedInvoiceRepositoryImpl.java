package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Invoice;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedInvoiceRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedInvoiceRepositoryImpl extends AbstractCustomizedDomainRepository<Invoice, String> implements CustomizedInvoiceRepository {
    public CustomizedInvoiceRepositoryImpl(MongoTemplate template) {
        super(template);
    }
}
