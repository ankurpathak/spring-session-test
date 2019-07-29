package com.github.ankurpathak.api.rest.controllor.dto;

import java.io.Serializable;

public class PhoneEmailPairDto implements Serializable {
    private final String phone;
    private final String email;

    public PhoneEmailPairDto(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public static PhoneEmailPairDto getInstance(String phone, String email){
        return new PhoneEmailPairDto(phone, email);
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
