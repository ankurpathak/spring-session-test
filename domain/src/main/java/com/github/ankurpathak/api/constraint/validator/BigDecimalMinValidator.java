package com.github.ankurpathak.api.constraint.validator;

import com.github.ankurpathak.api.constraint.BigDecimalMin;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class BigDecimalMinValidator implements ConstraintValidator<BigDecimalMin, BigDecimal> {

    private static final Logger log = LoggerFactory.getLogger(BigDecimalMinValidator.class);


    private BigDecimalMin config;
    private BigDecimal min;

    @Override
    public void initialize(BigDecimalMin config) {
        this.config = config;
        try{
            min = new BigDecimal(config.value());
        }catch (NumberFormatException ex){
            LogUtil.logStackTrace(log, ex);
        }
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value == null ? config.ignoreBlank() : value.compareTo(min) > 0;
    }
}
