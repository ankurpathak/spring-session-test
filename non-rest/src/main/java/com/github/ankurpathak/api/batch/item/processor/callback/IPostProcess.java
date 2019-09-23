package com.github.ankurpathak.api.batch.item.processor.callback;

import com.github.ankurpathak.api.batch.item.processor.DomainItemProcessor;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;

import java.io.Serializable;

@FunctionalInterface
public interface IPostProcess<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID>> {
    void doPostCreate(DomainItemProcessor<Tdto, ID, T> processor, Tdto tDto, T t);

    static <Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID>>  IPostProcess<Tdto, ID, T> postProcess() {
        return (processor, tDto, t) -> {};
    }
}
