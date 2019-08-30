package com.github.ankurpathak.api.service.callback;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.github.ankurpathak.api.service.impl.RestControllerService;
import com.mongodb.bulk.BulkWriteResult;

import java.io.Serializable;

@FunctionalInterface
public interface IPostCreateMany<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {
    void doPostCreateMany(RestControllerService rest, DomainDtoList<T, ID, TDto> dtoList, BulkWriteResult result);

}
