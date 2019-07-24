package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedProductRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTextRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedProductRepositoryImpl extends AbstractCustomizedDomainRepository<Product, String> implements CustomizedProductRepository {
    public CustomizedProductRepositoryImpl(MongoTemplate template) {
        super(template);
    }

}
