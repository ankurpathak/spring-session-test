package com.github.ankurpathak.api.domain.model;


import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection = Model.Product.PRODUCT)
public class Customer extends ExtendedDomain<CustomerId> {
    private String name;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public static Customer getInstance(){
        return new Customer();
    }

}
