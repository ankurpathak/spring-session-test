package com.github.ankurpathak.api.domain.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection = Model.Customer.CUSTOMER)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Customer extends ExtendedDomain<CustomerId> {

    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Customer id(CustomerId customerId) {
        super.id(customerId);
        return this;
    }

    public static Customer getInstance(){
        return new Customer();
    }

    public Customer address(Address address) {
        this.address = address;
        return this;
    }
}
