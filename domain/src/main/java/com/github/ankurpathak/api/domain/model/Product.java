package com.github.ankurpathak.api.domain.model;


import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;

@Document(collection = Model.Product.PRODUCT)
public class Product extends BusinessExtendedDomain<String> {

   // @Indexed(name = Model.Product.Index.NAME_IDX , sparse = true, unique = true)
   // Uncomment for testing duplicate key exception in createMany
    private String name;
    private String description;
    private BigDecimal amount = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private Boolean variable = false;


    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Boolean getVariable() {
        return variable;
    }

    public void setVariable(Boolean variable) {
        this.variable = variable;
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

    public Product tax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }


    public Product variable(Boolean variable) {
        this.variable = variable;
        return this;
    }
}
