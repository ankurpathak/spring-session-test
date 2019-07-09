package com.github.ankurpathak.api.domain.model;


import com.fasterxml.jackson.annotation.JsonView;
import com.github.ankurpathak.api.rest.controller.dto.View;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.io.Serializable;
import java.math.BigInteger;

public class ExtendedDomain<ID extends Serializable> extends Domain<ID> {

    @CreatedBy
    @JsonView({View.Public.class, View.Me.class})
    private BigInteger createdBy;

    @LastModifiedBy
    @JsonView({View.Public.class, View.Me.class})
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
