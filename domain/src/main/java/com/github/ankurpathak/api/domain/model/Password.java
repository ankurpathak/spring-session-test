package com.github.ankurpathak.api.domain.model;

import java.io.Serializable;

public class Password implements Serializable {
    private String value;
    private String tokenId;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }


    public Password value(String value) {
        this.value = value;
        return this;
    }

    public Password tokenId(String tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public static Password  getInstance(){
        return new Password();
    }
}
