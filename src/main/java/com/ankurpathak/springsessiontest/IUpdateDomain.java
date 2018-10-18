package com.ankurpathak.springsessiontest;

import java.io.Serializable;

@FunctionalInterface
public interface IUpdateDomain<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    T doUpdate(T t, TDto dto);

}