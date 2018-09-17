package com.ankurpathak.springsessiontest;

import java.io.Serializable;

public abstract class DomainDto<T extends Domain<ID>, ID extends Serializable> implements  Serializable {

    abstract public T toDomain(Class<?> type);

    abstract public T updateDomain(T domain, Class<?> type);

    public String domainName(){
        String name = this.getClass().getSimpleName();
        name = name != null ? name : "";
        int index = name.indexOf('D');
        index = index > -1 ? index : 0;
        return name.substring(0, index);
    }


    public interface Default {

    }


    public interface Register {

    }

    public interface PasswordReset {
    }
}
