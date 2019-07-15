package com.github.ankurpathak.api.rest.controller.callback;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.AbstractRestController;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;

import java.io.Serializable;
@FunctionalInterface
public interface IPreCreateOne<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {
    void doPreCreateOne(AbstractRestController<T,ID,TDto> restController, TDto dto);
}