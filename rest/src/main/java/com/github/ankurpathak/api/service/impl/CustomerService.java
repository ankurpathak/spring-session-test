package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomerRepository;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.PhoneEmailPairDto;
import com.github.ankurpathak.api.service.ICustomerService;
import com.github.ankurpathak.api.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends AbstractDomainService<Customer, CustomerId> implements ICustomerService {

    private final ICustomerRepository dao;
    private final IUserService userService;

    public CustomerService(ICustomerRepository dao, IUserService userService) {
        super(dao);
        this.dao = dao;
        this.userService = userService;
    }


    @Override
    public User processUser(Business business, CustomerDto dto){
        return userService.processUserForCustomer(business, dto);
    }


}
