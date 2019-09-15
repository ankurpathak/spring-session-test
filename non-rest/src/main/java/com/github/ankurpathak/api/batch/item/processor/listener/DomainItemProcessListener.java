package com.github.ankurpathak.api.batch.item.processor.listener;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import org.springframework.batch.core.ItemProcessListener;

import java.io.Serializable;


public class DomainItemProcessListener<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID>> implements ItemProcessListener<Tdto,  T> {
    private final Class<T> tClass;
    private final Class<Tdto> tdtoClass;

    public DomainItemProcessListener(Class<T> tClass, Class<Tdto> tdtoClass) {
        this.tClass = tClass;
        this.tdtoClass = tdtoClass;
    }

    @Override
    public void beforeProcess(Tdto item) { }

    @Override
    public void afterProcess(Tdto item, T result) { }

    @Override
    public void onProcessError(Tdto item, Exception e) { }
}
