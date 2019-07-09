package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.domain.model.Domain;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class DomainDtoList<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    @NotEmpty
    @NotNull
    private List<@NotNull @Valid TDto> dtos;


    public int size(){
        return dtos.size();
    }


    public List<TDto> getDtos() {
        return dtos;
    }

    public void setDtos(List<TDto> dtos) {
        this.dtos = dtos;
    }

    public TDto getDto(){
        return dtos.get(0);
    }
}
