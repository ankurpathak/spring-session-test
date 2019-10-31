package com.github.ankurpathak.api.domain.updater;

import com.github.ankurpathak.api.domain.model.Address;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;

public class CustomerUpdaters {
    public static IUpdateDomain<Customer, CustomerId, CustomerDto> updateCustomer() {
        return (customer, dto) -> customer
                .description(dto.getDescription())
                .address(Address.getInstance(dto, false));
    }
}