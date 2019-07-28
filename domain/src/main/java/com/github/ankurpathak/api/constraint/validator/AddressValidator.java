package com.github.ankurpathak.api.constraint.validator;

import com.github.ankurpathak.api.constraint.Address;
import com.github.ankurpathak.api.constraint.VariableOrAmount;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<Address, CustomerDto> {

    private Address config;

    @Override
    public void initialize(Address config) {
        this.config = config;
    }

    @Override
    public boolean isValid(CustomerDto dto, ConstraintValidatorContext context) {
       if(StringUtils.isEmpty(dto.getAddress())){
            return config.ignoreBlankAddress();
        }else{
           boolean isStateBlank = StringUtils.isEmpty(dto.getState());
           boolean isCityBlank  = StringUtils.isEmpty(dto.getCity());
           boolean isPincodeBlank = StringUtils.isEmpty(dto.getPincode());

           boolean combinedResult = !isStateBlank &&  !isCityBlank && !isPincodeBlank;

           if(!combinedResult){
               context.disableDefaultConstraintViolation();
               if(isStateBlank){
                   context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode("state").addConstraintViolation();
               }
               if(isCityBlank){
                   context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode("pincode").addConstraintViolation();
               }
               if(isPincodeBlank){
                   context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode("pincode").addConstraintViolation();
               }
           }
           return combinedResult;
        }
    }
}
