package com.github.ankurpathak.app.controller.rest.dto;

import com.github.ankurpathak.app.controller.rest.dto.DomainDto;
import com.github.ankurpathak.app.domain.model.Domain;

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
