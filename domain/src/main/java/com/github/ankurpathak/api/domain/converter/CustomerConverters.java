package com.github.ankurpathak.api.domain.converter;

import com.github.ankurpathak.api.domain.model.*;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;

public class CustomerConverters {
    public static IToDomain<Customer, CustomerId, CustomerDto> createOne = dto -> Customer
            .getInstance()
            .id(CustomerId.getInstance(dto.getUserId(), dto.getBusinessId()))
            .description(dto.getDescription())
            .address(Address.getInstance(dto));

}
