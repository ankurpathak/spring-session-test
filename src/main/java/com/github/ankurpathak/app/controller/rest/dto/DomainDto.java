package com.github.ankurpathak.app.controller.rest.dto;

import com.github.ankurpathak.app.domain.converter.IToDomain;
import com.github.ankurpathak.app.domain.updater.IUpdateDomain;
import com.github.ankurpathak.app.domain.model.Domain;

import java.io.Serializable;

public abstract class DomainDto<T extends Domain<ID>, ID extends Serializable> implements  Serializable {

    @SuppressWarnings("unchecked")
    public  <TDto extends DomainDto<T, ID>> T toDomain(IToDomain<T, ID, TDto> converter){
        TDto dto = (TDto) this;
        return converter.toDomain(dto);
    }

    @SuppressWarnings("unchecked")
    public <TDto extends DomainDto<T, ID>> T updateDomain(T t, IUpdateDomain<T, ID, TDto> updater){
        TDto dto = (TDto) this;
        return updater.doUpdate(t, dto);
    }

    public String domainName(){
        String name = this.getClass().getSimpleName();
        name = name != null ? name : "";
        int index = name.indexOf('D');
        index = index > -1 ? index : 0;
        return name.substring(0, index);
    }


    public interface Default {

    }



}
