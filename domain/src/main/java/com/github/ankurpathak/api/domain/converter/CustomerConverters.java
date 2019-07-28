package com.github.ankurpathak.api.domain.converter;

import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;

public class CustomerConverters {
    public static IToDomain<Customer, CustomerId, CustomerDto> createOne = dto -> Customer
            .getInstance();

}
