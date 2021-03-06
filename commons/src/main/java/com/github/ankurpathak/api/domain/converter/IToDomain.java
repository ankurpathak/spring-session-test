package com.github.ankurpathak.api.domain.converter;

import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.domain.model.Domain;

import java.io.Serializable;

@FunctionalInterface
public interface IToDomain<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    T toDomain(TDto dto);

}