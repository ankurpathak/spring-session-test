package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.IBusinessRepository;
import com.github.ankurpathak.api.service.IBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.require;


@Service
public class BusinessService extends AbstractDomainService<Business, BigInteger> implements IBusinessService {
    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);

    private final IBusinessRepository dao;

    protected BusinessService(IBusinessRepository dao) {
        super(dao);
        this.dao = dao;
    }


    @Override
    public Business create(Business entity) {
        require(entity, notNullValue());
        return dao.persist(entity);
    }
}
