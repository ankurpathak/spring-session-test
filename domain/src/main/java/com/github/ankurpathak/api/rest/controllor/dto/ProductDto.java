package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.constant.CsvConstant;
import com.github.ankurpathak.api.constraint.BigDecimalMin;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.opencsv.BooleanBeanField;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

//@VariableOrAmount(groups = {Default.class})
public class ProductDto extends DomainDto<Product, String> {
    @NotBlank
    @CsvBindByName(column = CsvConstant.Product.NAME, required = true)
    private String name;


    @BigDecimalMin(value = "0")
    @CsvBindByName(column = CsvConstant.Product.AMOUNT, required = true)
    @NotNull
    private BigDecimal amount;

    @BigDecimalMin(value = "0")
    @CsvBindByName(column = CsvConstant.Product.TAX, required = true)
    @NotNull
    private BigDecimal tax;

    @NotNull
    @CsvCustomBindByName(column = CsvConstant.Product.VARIABLE, converter = BooleanBeanField.class, required = true)
    private Boolean variable;

    @CsvBindByName(column = CsvConstant.Product.DESCRIPTION)
    private String description;
    private List<String> images;
    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVariable() {
        return variable;
    }

    public void setVariable(Boolean variable) {
        this.variable = variable;
    }

    public static ProductDto getInstance() {
        return new ProductDto();
    }


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

    public ProductDto amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ProductDto tax(BigDecimal tax) {
        this.tax = tax;
        return this;
    }


    public ProductDto variable(Boolean variable) {
        this.variable = variable;
        return this;
    }


    public ProductDto description(String description) {
        this.description = description;
        return this;
    }

    public ProductDto images(List<String> images) {
        this.images = images;
        return this;
    }


    public interface Secondary {
    }


}
