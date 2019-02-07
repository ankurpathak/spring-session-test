package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.model.Domain;

import java.io.Serializable;

@FunctionalInterface
public interface IToDomain<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    T toDomain(TDto dto);

}