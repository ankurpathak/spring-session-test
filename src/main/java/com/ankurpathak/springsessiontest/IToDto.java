package com.ankurpathak.springsessiontest;

import com.ankurpathak.springsessiontest.Domain;
import com.ankurpathak.springsessiontest.DomainDto;

import java.io.Serializable;

@FunctionalInterface
public interface IToDto<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    TDto toDto(T t);
}