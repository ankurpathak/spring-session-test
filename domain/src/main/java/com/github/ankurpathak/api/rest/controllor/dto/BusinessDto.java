package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.primitive.bean.constraints.string.Any;

import javax.validation.constraints.NotBlank;
import java.math.BigInteger;

public class BusinessDto extends DomainDto<Business, BigInteger> {
    @NotBlank(groups = {DomainDto.Default.class})
    private String name;
    @Any(value = {}, groups = {DomainDto.Default.class})
    @NotBlank(groups = {DomainDto.Default.class})
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusinessDto name(String name) {
        this.name = name;
        return this;
    }

    public BusinessDto type(String type) {
        this.type = type;
        return this;
    }
}
