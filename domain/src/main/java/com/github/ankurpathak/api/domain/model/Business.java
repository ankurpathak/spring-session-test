package com.github.ankurpathak.api.domain.model;

import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;

@Document(collection = Model.Business.BUSINESS)
public class Business extends ExtendedDomain<BigInteger> implements Serializable {
    private String name;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Business name(String name) {
        this.name = name;
        return this;
    }

    public Business type(String type) {
        this.type = type;
        return this;
    }

    public static Business getInstance(){
        return new Business();
    }


    public interface BusinessType {
    }
}
