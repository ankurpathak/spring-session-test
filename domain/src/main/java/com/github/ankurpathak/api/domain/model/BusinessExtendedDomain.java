package com.github.ankurpathak.api.domain.model;


import java.io.Serializable;
import java.math.BigInteger;

public class BusinessExtendedDomain<ID extends Serializable> extends ExtendedDomain<ID> {
    private BigInteger businessId;

    public BigInteger getBusinessId() {
        return businessId;
    }



    public void setBusinessId(BigInteger businessId) {
        this.businessId = businessId;
    }

    public BusinessExtendedDomain businessId(BigInteger businessId) {
        this.businessId = businessId;
        return this;
    }

}
