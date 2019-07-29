package com.github.ankurpathak.api.domain.model;

import org.springframework.data.annotation.PersistenceConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class CustomerId implements Serializable {

    private final BigInteger businessId;

    public BigInteger getBusinessId() {
        return businessId;
    }

    @PersistenceConstructor
    public CustomerId(BigInteger businessId, BigInteger userId) {
        require(businessId, notNullValue());
        require(userId, notNullValue());
        this.businessId = businessId;
        this.userId = userId;
    }


    public static CustomerId getInstance(BigInteger uerrId, BigInteger businessId){
        return new CustomerId(uerrId, businessId);
    }


    private final BigInteger userId;


    public BigInteger getUserId() {
        return userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return businessId.equals(that.businessId) &&
                userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessId, userId);
    }

    @Override
    public String toString() {
        return String.format("%s/%s", userId, businessId);
    }
}
