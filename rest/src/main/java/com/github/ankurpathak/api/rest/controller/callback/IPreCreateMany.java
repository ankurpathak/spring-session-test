package com.github.ankurpathak.api.rest.controller.callback;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.AbstractRestController;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;

import java.io.Serializable;
import java.math.BigInteger;

@FunctionalInterface
public interface IPreCreateMany<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {
    void doPreCreateMany(AbstractRestController<T, ID, TDto> restController, DomainDtoList<T, ID, TDto> dtoList);
}
