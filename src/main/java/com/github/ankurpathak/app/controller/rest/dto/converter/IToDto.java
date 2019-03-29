package com.github.ankurpathak.app.controller.rest.dto.converter;

import com.github.ankurpathak.app.controller.rest.dto.DomainDto;
import com.github.ankurpathak.app.domain.model.Domain;

import java.io.Serializable;

@FunctionalInterface
public interface IToDto<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    TDto toDto(T t);
}