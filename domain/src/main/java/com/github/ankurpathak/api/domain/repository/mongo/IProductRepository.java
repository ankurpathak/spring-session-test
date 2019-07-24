package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedProductRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTextRepository;

public interface IProductRepository extends ExtendedMongoRepository<Product, String>, CustomizedProductRepository {
}
