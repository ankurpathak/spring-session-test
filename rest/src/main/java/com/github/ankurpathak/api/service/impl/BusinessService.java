package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.repository.mongo.IBusinessRepository;
import com.github.ankurpathak.api.service.IBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;


@Service
public class BusinessService extends AbstractDomainService<Business, BigInteger> implements IBusinessService {
    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);

    private final IBusinessRepository dao;

    protected BusinessService(IBusinessRepository dao) {
        super(dao);
        this.dao = dao;
    }
}
