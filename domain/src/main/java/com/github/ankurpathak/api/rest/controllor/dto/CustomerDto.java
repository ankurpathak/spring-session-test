package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.constant.CsvConstant;
import com.github.ankurpathak.api.constraint.Address;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.primitive.bean.constraints.string.Contact;
import com.github.ankurpathak.primitive.bean.constraints.string.Email;
import com.opencsv.bean.CsvBindByName;

import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
@Address
public class CustomerDto extends DomainDto<Customer,CustomerId> {

    @CsvBindByName(column = CsvConstant.Customer.NAME)
    private String name;

    @CsvBindByName(column = CsvConstant.Customer.EMAIL)
    @Email
    private String email;

    @NotBlank
    @Contact
    @CsvBindByName(column = CsvConstant.Customer.PHONE, required = true)
    private String phone;


    @CsvBindByName(column = CsvConstant.Customer.ADDRESS)
    private String address;

    @CsvBindByName(column = CsvConstant.Customer.STATE)
    private String state;

    @CsvBindByName(column = CsvConstant.Customer.CITY)
    private String city;

    @CsvBindByName(column = CsvConstant.Customer.PIN_CODE)
    private String pinCode;

    private BigInteger userId;

    private BigInteger businessId;



    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getBusinessId() {
        return businessId;
    }

    public void setBusinessId(BigInteger businessId) {
        this.businessId = businessId;
    }

    public static CustomerDto getInstance(){
        return new CustomerDto();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public CustomerDto name(String name) {
        this.name = name;
        return this;
    }

    public CustomerDto email(String email) {
        this.email = email;
        return this;
    }

    public CustomerDto phone(String phone) {
        this.phone = phone;
        return this;
    }

    public CustomerDto address(String address) {
        this.address = address;
        return this;
    }

    public CustomerDto state(String state) {
        this.state = state;
        return this;
    }

    public CustomerDto city(String city) {
        this.city = city;
        return this;
    }

    public CustomerDto pinCode(String pinCode) {
        this.pinCode = pinCode;
        return this;
    }

    public CustomerDto userId(BigInteger userId) {
        this.userId = userId;
        return this;
    }

    public CustomerDto businessId(BigInteger businessId) {
        this.businessId = businessId;
        return this;
    }


}
