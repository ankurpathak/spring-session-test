package com.github.ankurpathak.api.service.dto;


import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


public final class SmtpCredential implements Serializable {

    public static final  SmtpCredential EMPTY_INSTANCE = getInstance();


    public static final SmtpCredential getInstance(){
        return new SmtpCredential();
    }


    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;


    private String name;

    @AssertTrue
    private boolean checked;

    public SmtpCredential() {
        checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
