package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class ProductDto extends DomainDto<Product, String> {
    @NotBlank(groups = {Default.class})
    private String name;


    @DecimalMin(value = "0", groups = {Default.class})
    private BigDecimal amount;

    @DecimalMin(value = "0", groups = {Default.class})
    private BigDecimal tax;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public ProductDto name(String name) {
        this.name = name;
        return this;
    }


    public interface Secondary {}


}
