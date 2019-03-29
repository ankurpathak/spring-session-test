package com.github.ankurpathak.app.service.dto;

import java.io.Serializable;

public class SocialProfile implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String imgUrl;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public static SocialProfile getInstance(){
        return new SocialProfile();
    }


    public SocialProfile firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public SocialProfile lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public SocialProfile email(String email) {
        this.email = email;
        return this;
    }

    public SocialProfile imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
