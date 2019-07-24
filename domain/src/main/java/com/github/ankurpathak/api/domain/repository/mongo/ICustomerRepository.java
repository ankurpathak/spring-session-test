package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedCustomerRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedProductRepository;

public interface ICustomerRepository extends ExtendedMongoRepository<Customer, CustomerId>, CustomizedCustomerRepository {
}
