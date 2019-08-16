package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomerRepository;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.ICustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService extends AbstractDomainService<Customer, CustomerId> implements ICustomerService {

    private final ICustomerRepository dao;
    private final CustomUserDetailsService userDetailsService;

    public CustomerService(ICustomerRepository dao, CustomUserDetailsService userDetailsService) {
        super(dao);
        this.dao = dao;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public User processUser(Business business, CustomerDto dto){
        return userDetailsService.getUserService().processUserForCustomer(business, dto);
    }

    @Override
    public List<User> processUsers(Business business, Map<String, CustomerDto> dtos) {
        return userDetailsService.getUserService().processUserForCustomers(business, dtos);
    }


}
