package com.github.ankurpathak.app.domain.updater;

import com.github.ankurpathak.app.controller.rest.dto.DomainDto;
import com.github.ankurpathak.app.domain.model.Domain;

import java.io.Serializable;

@FunctionalInterface
public interface IUpdateDomain<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    T doUpdate(T t, TDto dto);

}