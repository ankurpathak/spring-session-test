package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;

import java.util.List;
import java.util.Map;

public interface ICustomerService extends IDomainService<Customer, CustomerId> {
    User processUser(Business business, CustomerDto dto);
    List<User> processUsers(Business business, Map<String, CustomerDto> dtos);
}
