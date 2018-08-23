package com.ankurpathak.springsessiontest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class DomainDtoList<T extends DomainDto<ID>, ID extends Serializable> {

    @NotEmpty
    @NotNull
    private List<@NotNull @Valid T> dtos;


    public int size(){
        return dtos.size();
    }


    public List<T> getDtos() {
        return dtos;
    }

    public void setDtos(List<T> dtos) {
        this.dtos = dtos;
    }

    public T getDto(){
        return dtos.get(0);
    }
}
