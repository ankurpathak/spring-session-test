package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestDto {
    private  String username;
    private String password;



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequestDto() {
    }


}
