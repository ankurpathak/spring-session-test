package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.mapping.Document;
import com.github.ankurpathak.api.domain.model.SmtpCredential;

import java.io.Serializable;
import java.math.BigInteger;

@Document(collection = Model.Business.BUSINESS)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Business extends ExtendedDomain<BigInteger> implements Serializable {
    private String name;
    private SmtpCredential smtpCredential;

    public SmtpCredential getSmtpCredential() {
        return smtpCredential;
    }

    public void setSmtpCredential(SmtpCredential smtpCredential) {
        this.smtpCredential = smtpCredential;
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


    @Override
    public Business addTag(String tag) {
        super.addTag(tag);
        return this;
    }

    public static Business getInstance(){
        return new Business();
    }

}
