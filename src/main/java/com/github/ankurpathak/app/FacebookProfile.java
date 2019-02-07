package com.github.ankurpathak.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class FacebookProfile extends SocialProfile {


    @Override
    @JsonSetter("last_name")
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    @JsonSetter("first_name")
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }



    @Override
    @JsonProperty("profile_pic")
    public void setImgUrl(String imgUrl) {
        super.setImgUrl(imgUrl);
    }
}
