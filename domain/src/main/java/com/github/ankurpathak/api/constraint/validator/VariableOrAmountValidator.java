package com.github.ankurpathak.api.constraint.validator;

import com.github.ankurpathak.api.constraint.VariableOrAmount;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VariableOrAmountValidator implements ConstraintValidator<VariableOrAmount, ProductDto> {

    private VariableOrAmount config;

    @Override
    public void initialize(VariableOrAmount config) {
        this.config = config;
    }

    @Override
    public boolean isValid(ProductDto productDto, ConstraintValidatorContext context) {
       return (productDto.getVariable() == null) ? config.ignoreBlankVariable() : (productDto.getAmount() != null && !productDto.getVariable()) ^ productDto.getVariable();
    }
}
