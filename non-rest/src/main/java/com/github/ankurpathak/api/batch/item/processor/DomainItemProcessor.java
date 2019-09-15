package com.github.ankurpathak.api.batch.item.processor;

import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.exception.ValidationException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.Serializable;
import java.util.List;

public class DomainItemProcessor<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID> > implements ItemProcessor<Tdto, T> {

    private final LocalValidatorFactoryBean validator;
    private final IToDomain<T, ID, Tdto> converter;
    private final Class<T> tClass;
    private final Class<Tdto> dDtoClass;

    public DomainItemProcessor(LocalValidatorFactoryBean validator, IToDomain<T, ID, Tdto> converter, Class<T> tClass, Class<Tdto> dDtoClass) {
        this.validator = validator;
        this.converter = converter;
        this.tClass = tClass;
        this.dDtoClass = dDtoClass;
    }


    @Override
    public T process(Tdto i) throws Exception {
        BindException result = new BindException(i, i.getClass().getSimpleName());
        validator.validate(i, result);
        if(result.hasErrors()){
            ValidationException vdEx = new ValidationException(List.of(result), ApiCode.VALIDATION, ApiMessages.VALIDATION);
            throw vdEx;
        }
        return converter.toDomain(i);
    }

}
