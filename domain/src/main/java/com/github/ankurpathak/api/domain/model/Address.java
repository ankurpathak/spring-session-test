package com.github.ankurpathak.api.domain.model;

import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {
    public static final String TAG_PRIMARY = "primary";
    public static final String TAG_ADDED_BY_BUSINESS = "added_by_business_%s";
    private String name;
    private Contact email;
    private Contact phone;

    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String tag;


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public static Address getInstance(){
        return new Address().tag(TAG_PRIMARY);
    }


    public static Address getInstance(CustomerDto dto){
        Address address = Address.getInstance()
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(Contact.getInstance(dto.getPhone()))
                .state(dto.getState())
                .city(dto.getCity())
                .pinCode(dto.getPinCode());
        if(StringUtils.isNotEmpty(dto.getEmail())){
            address.email(Contact.getInstance(dto.getEmail()));
        }
        return address;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getEmail() {
        return email;
    }

    public void setEmail(Contact email) {
        this.email = email;
    }

    public Contact getPhone() {
        return phone;
    }

    public void setPhone(Contact phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Address name(String name) {
        this.name = name;
        return this;
    }

    public Address email(Contact email) {
        this.email = email;
        return this;
    }

    public Address phone(Contact phone) {
        this.phone = phone;
        return this;
    }

    public Address address(String address) {
        this.address = address;
        return this;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }

    public Address state(String state) {
        this.state = state;
        return this;
    }

    public Address pinCode(String pinCode) {
        this.pinCode = pinCode;
        return this;
    }

    public Address tag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return Objects.equals(name, address1.name) &&
                Objects.equals(email, address1.email) &&
                Objects.equals(phone, address1.phone) &&
                Objects.equals(address, address1.address) &&
                Objects.equals(city, address1.city) &&
                Objects.equals(state, address1.state) &&
                Objects.equals(pinCode, address1.pinCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, phone, address, city, state, pinCode);
    }
}
