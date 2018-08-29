package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@JsonInclude(Include.NON_EMPTY)

public  class Token implements Serializable {
    private String value;
    private Date expiryDate;
    private static final int EXPIRATION_IN_MINUTES = 24 * 60;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    private final Date calculateExpiryDate(int expirationInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expirationInMinutes);
        return cal.getTime();
    }

    public final void updateToken(String token) {
        this.value = token;
        this.expiryDate = this.calculateExpiryDate(EXPIRATION_IN_MINUTES);
    }

    public Token() {
    }

    public Token(String value) {
        this.value = value;
        this.expiryDate = this.calculateExpiryDate(EXPIRATION_IN_MINUTES);
    }

    public Token(String value, User user) {
        this(value);
        user.setPasswordResetToken(this);
    }


    public enum TokenStatus {
        VALID,
        INVALID,
        EXPIRED
    }


}
