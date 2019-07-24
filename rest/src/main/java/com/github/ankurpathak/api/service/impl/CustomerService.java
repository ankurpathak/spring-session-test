package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.Mail;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomerRepository;
import com.github.ankurpathak.api.domain.repository.mongo.IMailRepository;
import com.github.ankurpathak.api.service.ICustomerService;
import com.github.ankurpathak.api.service.IMailService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends AbstractDomainService<Customer, CustomerId> implements ICustomerService {

    private final ICustomerRepository dao;

    public CustomerService(ICustomerRepository dao) {
        super(dao);
        this.dao = dao;
    }


}
