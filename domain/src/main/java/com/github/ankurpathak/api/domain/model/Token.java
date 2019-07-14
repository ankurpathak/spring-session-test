package com.github.ankurpathak.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.ankurpathak.api.constant.Model;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@JsonInclude(Include.NON_EMPTY)
@Document(collection = Model.Token.TOKEN)
public class Token extends Domain<String> implements Serializable {

    private String value;

    @Indexed(name = Model.Token.Index.TOKEN_EXPIRY_IDX, expireAfterSeconds= EXPIRATION_IN_SECONDS)
    private Instant expiry;
    private static final int EXPIRATION_IN_MINUTES = 30;

    private static final int EXPIRATION_IN_SECONDS = 30 * 60;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    private final Instant calculateExpiryDate(int expirationInMinutes) {
        return Instant.now().plus(expirationInMinutes,ChronoUnit.MINUTES);
    }




    public  static Token getInstance(){

        return new Token();
    }



    public Token() {
        this.expiry = this.calculateExpiryDate(EXPIRATION_IN_MINUTES);
    }

    public Token value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public Token id(String s) {
        super.id(s);
        return this;
    }

    public Token expiry(Instant expiry) {
        this.expiry = expiry;
        return this;
    }



    public enum TokenStatus {
        VALID,
        INVALID,
        EXPIRED
    }


}
