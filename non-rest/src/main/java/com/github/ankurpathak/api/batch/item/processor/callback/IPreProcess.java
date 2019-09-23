package com.github.ankurpathak.api.batch.item.processor.callback;

import com.github.ankurpathak.api.batch.item.processor.DomainItemProcessor;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;

import java.io.Serializable;

@FunctionalInterface
public interface IPreProcess<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID>> {
    void doPreCreate(DomainItemProcessor<Tdto, ID, T> processor, Tdto tDto);

    static <Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID>>  IPreProcess<Tdto, ID, T> preProcess() {
        return (processor, tDto) -> {};
    }
}
