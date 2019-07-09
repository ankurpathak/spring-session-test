package com.github.ankurpathak.api.controllor.rest.dto;

import com.github.ankurpathak.password.bean.constraints.ContainUppercase;
import com.github.ankurpathak.password.bean.constraints.PasswordMatches;

import java.io.Serializable;

@PasswordMatches(password = "username", confirmPassword = "usernameConfirm", showErrorOnConfirmPassword = false)
public class UsernameDto implements Serializable {

    @Override
    public String toString() {
        return "UsernameDto{" +
                "username='" + username + '\'' +
                '}';
    }


    @ContainUppercase
    private String username;


    private String usernameConfirm;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsernameConfirm() {
        return usernameConfirm;
    }

    public void setUsernameConfirm(String usernameConfirm) {
        this.usernameConfirm = usernameConfirm;
    }
}
