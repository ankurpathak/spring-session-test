package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.CustomerId;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.primitive.bean.constraints.string.Contact;
import com.github.ankurpathak.primitive.bean.constraints.string.Email;

import javax.validation.constraints.NotBlank;

public class CustomerDto extends DomainDto<Customer,CustomerId> {

    private String name;

    @Email
    private String email;

    @Contact
    @NotBlank
    private String phone;


    private String address;

    private String state;

    private String city;

    private String pincode;



    public CustomerDto getInstance(){
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public CustomerDto pincode(String pincode) {
        this.pincode = pincode;
        return this;
    }
}
