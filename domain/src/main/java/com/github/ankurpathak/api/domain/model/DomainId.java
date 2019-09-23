package com.github.ankurpathak.api.domain.model;

import java.io.Serializable;

public class DomainId<ID extends Serializable> {
    private String name;
    private ID id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public DomainId name(String name) {
        this.name = name;
        return this;
    }

    public DomainId id(ID id) {
        this.id = id;
        return this;
    }
}
