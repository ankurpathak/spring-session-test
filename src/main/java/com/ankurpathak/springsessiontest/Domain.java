package com.ankurpathak.springsessiontest;


import java.io.Serializable;

abstract public class Domain<T> implements Serializable {
    private  T id;


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Domain<?> domain = (Domain<?>) o;

        return id != null ? id.equals(domain.id) : domain.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public Domain id(T id) {
        this.id = id;
        return this;
    }



}
