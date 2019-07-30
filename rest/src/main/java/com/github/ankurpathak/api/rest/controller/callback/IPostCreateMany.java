package com.github.ankurpathak.api.rest.controller.callback;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.AbstractRestController;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.DomainDtoList;
import com.mongodb.bulk.BulkWriteResult;

import java.io.Serializable;
import java.math.BigInteger;

@FunctionalInterface
public interface IPostCreateMany<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {
    void doPostCreateMany(AbstractRestController<T, ID, TDto> restController, DomainDtoList<T, ID, TDto> dtoList, BulkWriteResult result);
}
