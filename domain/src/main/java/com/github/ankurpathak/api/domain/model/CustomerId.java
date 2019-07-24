package com.github.ankurpathak.api.domain.model;

import org.springframework.data.annotation.PersistenceConstructor;

import java.io.Serializable;
import java.math.BigInteger;

public class CustomerId implements Serializable {

    private BigInteger businessId;

    public BigInteger getBusinessId() {
        return businessId;
    }

    @PersistenceConstructor
    public CustomerId(BigInteger businessId, BigInteger userId) {
        this.businessId = businessId;
        this.userId = userId;
    }

    public void setBusinessId(BigInteger businessId) {
        this.businessId = businessId;
    }

    public CustomerId businessId(BigInteger businessId) {
        this.businessId = businessId;
        return this;
    }

    private BigInteger userId;


    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public CustomerId userId(BigInteger userId) {
        this.userId = userId;
        return this;
    }

}
