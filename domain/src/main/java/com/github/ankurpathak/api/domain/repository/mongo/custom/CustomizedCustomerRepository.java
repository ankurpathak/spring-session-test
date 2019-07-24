package com.github.ankurpathak.api.domain.repository.mongo.custom;

import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomizedDomainRepository;

public interface CustomizedCustomerRepository extends ICustomizedDomainRepository<Customer, CustomerId> {
}
