package com.github.ankurpathak.api.domain.repository.mongo.custom;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomizedDomainRepository;

import java.math.BigInteger;

public interface CustomizedProductRepository extends ICustomizedDomainRepository<Product, String> {
}
