package com.github.ankurpathak.api.rest.controllor.dto;

import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.primitive.bean.constraints.springframework.web.file.FileType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Valid
public class DomainDtoList<T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> {

    @NotNull(groups = {DomainDto.Default.class})
    private List<@NotNull(groups = DomainDto.Default.class)  @Valid TDto> dtos;

    @NotNull(groups = {Upload.class})
    @FileType(mimes = {"text/csv"}, groups = {Upload.class})
    private MultipartFile csv;

    public MultipartFile getCsv() {
        return csv;
    }

    public void setCsv(MultipartFile csv) {
        this.csv = csv;
    }

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

    public DomainDtoList<T, ID, TDto> dtos(List<TDto> dtos) {
        this.dtos = dtos;
        return this;
    }

    public DomainDtoList<T, ID, TDto> csv(MultipartFile csv) {
        this.csv = csv;
        return this;
    }




    public interface Upload {

    }

    public  static <T extends Domain<ID>, ID extends Serializable, TDto extends DomainDto<T, ID>> DomainDtoList<T, ID, TDto> getInstance(){
        return new DomainDtoList<>();
    }
}
