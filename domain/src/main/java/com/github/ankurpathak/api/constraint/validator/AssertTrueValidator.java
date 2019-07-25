package com.github.ankurpathak.api.constraint.validator;

import com.github.ankurpathak.api.constraint.AssertTrue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AssertTrueValidator implements ConstraintValidator<AssertTrue, Boolean> {

    private AssertTrue config;
    @Override
    public void initialize(AssertTrue config) {
        this.config = config;
    }

    public AssertTrueValidator() {
    }

    public boolean isValid(Boolean bool, ConstraintValidatorContext constraintValidatorContext) {
        return bool == null ? config.ignoreBlank() : bool;
    }
}