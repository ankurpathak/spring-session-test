package com.ankurpathak.springsessiontest;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public abstract class DomainDto<ID extends Serializable> implements  Serializable {

    abstract public Domain<ID> toDomain();

    abstract public Domain<ID> updateDomain(Domain<ID> domain);

    public String domainName(){
        String name = this.getClass().getSimpleName();
        name = name != null ? name : "";
        int index = name.indexOf('D');
        index = index > -1 ? index : 0;
        return name.substring(0, index);
    }
}
