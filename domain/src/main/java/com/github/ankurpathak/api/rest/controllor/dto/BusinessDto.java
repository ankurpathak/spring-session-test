package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.primitive.bean.constraints.string.Any;
import com.github.ankurpathak.primitive.bean.constraints.string.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;
import java.math.BigInteger;

public class BusinessDto extends DomainDto<Business, BigInteger> {
    @NotBlank(groups = {Default.class})
    private String name;
    @Any(value = {"RENTAL", "GYM", "PG", "ED", "HS" }, groups = {Default.class})
    @NotBlank(groups = {Account.class})
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

    @Email(groups = {Account.class})
    public String email;

    public static BusinessDto getInstance(){
        return new BusinessDto();
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BusinessDto name(String name) {
        this.name = name;
        return this;
    }

    public BusinessDto type(String type) {
        this.type = type;
        return this;
    }

    public BusinessDto email(String email) {
        this.email = email;
        return this;
    }

    public interface Account {}

}
