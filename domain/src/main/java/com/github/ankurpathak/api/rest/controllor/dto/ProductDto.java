package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.constant.CsvConstant;
import com.github.ankurpathak.api.constraint.AssertTrue;
import com.github.ankurpathak.api.constraint.BigDecimalMin;
import com.github.ankurpathak.api.constraint.VariableOrAmount;
import com.github.ankurpathak.api.csv.BooleanBeanField;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@VariableOrAmount(groups = {DomainDto.Default.class})
public class ProductDto extends DomainDto<Product, String> {
    @NotBlank(groups = {Default.class})
    @CsvBindByName(column = CsvConstant.Product.NAME)
    private String name;


    @BigDecimalMin(value = "0", groups = {Default.class}, inclusive = false)
    @CsvBindByName(column = CsvConstant.Product.AMOUNT)
    private BigDecimal amount;

    @BigDecimalMin(value = "0", groups = {Default.class}, inclusive = false)
    @CsvBindByName(column = CsvConstant.Product.TAX)
    private BigDecimal tax;

   @NotNull(groups = {Default.class})
   @CsvCustomBindByName(column = CsvConstant.Product.VARIABLE, converter = BooleanBeanField.class)
   private Boolean variable;

    @CsvBindByName(column = CsvConstant.Product.DESCRIPTION)
    private String description;



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

    public static ProductDto getInstance(){
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




    public interface Secondary {}


}
