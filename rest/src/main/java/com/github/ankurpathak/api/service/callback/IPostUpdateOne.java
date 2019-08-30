package com.github.ankurpathak.api.service.callback;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.service.impl.RestControllerService;

import java.io.Serializable;

@FunctionalInterface
public interface IPostUpdateOne<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {
    void doPostUpdateOne(RestControllerService rest, T domain, TDto dto);
}
