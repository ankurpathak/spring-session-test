package com.ankurpathak.springsessiontest;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.io.Serializable;
import java.math.BigInteger;

public class ExtendedDomain<ID extends Serializable> extends Domain<ID> {

    @CreatedBy
    private BigInteger createdBy;

    @LastModifiedBy
    private BigInteger updatedBy;


    public ExtendedDomain() {
        createdBy = updatedBy = SecurityUtil.getMe().map(User::getId).orElse(User.ANONYMOUS_ID);
    }

    public BigInteger getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(BigInteger createdBy) {
        this.createdBy = createdBy;
    }

    public BigInteger getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(BigInteger updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ExtendedDomain createdBy(BigInteger createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ExtendedDomain updatedBy(BigInteger updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}
