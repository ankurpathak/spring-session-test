package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.repository.mongo.IProductRepository;
import com.github.ankurpathak.api.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService extends AbstractDomainService<Product, String> implements IProductService {

    private final IProductRepository dao;

    public ProductService(IProductRepository dao) {
        super(dao);
        this.dao = dao;
    }

}
