package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedCustomerRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedCustomerRepositoryImpl extends AbstractCustomizedDomainRepository<Customer, CustomerId> implements CustomizedCustomerRepository {
    public CustomizedCustomerRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
