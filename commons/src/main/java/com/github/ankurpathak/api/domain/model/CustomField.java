package com.github.ankurpathak.api.domain.model;

import java.io.Serializable;

public class CustomField implements Serializable {
    private String name;
    private Object value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public CustomField getInstance(){
        return new CustomField();
    }


    public CustomField name(String name) {
        this.name = name;
        return this;
    }

    public CustomField value(Object value) {
        this.value = value;
        return this;
    }
}
