package com.github.ankurpathak.api.domain.model;


import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;

@Document(collection = Model.Product.PRODUCT)
public class Product extends BusinessExtendedDomain<String> {

    @Indexed(name = Model.Product.Index.NAME_IDX , sparse = true, unique = true)
    private String name;
    private String description;
    private BigDecimal amount;
    private BigInteger tax;

    


    public BigInteger getTax() {
        return tax;
    }

    public void setTax(BigInteger tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Product name(String name) {
        this.name = name;
        return this;
    }

    public static Product getInstance(){
        return new Product();
    }


    @Override
    public Product businessId(BigInteger businessId) {
        super.businessId(businessId);
        return this;
    }


    public Product description(String description) {
        this.description = description;
        return this;
    }

    public Product amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Product tax(BigInteger tax) {
        this.tax = tax;
        return this;
    }
}
