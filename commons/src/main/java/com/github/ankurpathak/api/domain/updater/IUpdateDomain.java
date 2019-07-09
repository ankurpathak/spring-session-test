package com.github.ankurpathak.api.domain.updater;

import com.github.ankurpathak.api.controller.rest.dto.DomainDto;
import com.github.ankurpathak.api.domain.model.Domain;

import java.io.Serializable;

@FunctionalInterface
public interface IUpdateDomain<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {
    T doUpdate(T t, TDto dto);
}