package com.github.ankurpathak.api.batch.item.processor;

import com.github.ankurpathak.api.batch.item.processor.callback.IPostProcess;
import com.github.ankurpathak.api.batch.item.processor.callback.IPreProcess;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.exception.ValidationException;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DomainItemProcessor<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID> > implements ItemProcessor<Tdto, T> {
    private final LocalValidatorFactoryBean validator;
    private final IToDomain<T, ID, Tdto> converter;
    private final Class<T> tClass;
    private final Class<Tdto> dDtoClass;
    private final IPreProcess<Tdto, ID, T> preProcess;
    private final IPostProcess<Tdto, ID, T> postProcess;

    public DomainItemProcessor(LocalValidatorFactoryBean validator, IToDomain<T, ID, Tdto> converter, Class<T> tClass, Class<Tdto> dDtoClass, IPreProcess<Tdto, ID, T> preProcess, IPostProcess<Tdto, ID, T> postProcess) {
        this.validator = validator;
        this.converter = converter;
        this.tClass = tClass;
        this.dDtoClass = dDtoClass;
        this.preProcess = preProcess;
        this.postProcess = postProcess;
    }


    @Override
    public T process(Tdto i) throws Exception {
        BindException result = new BindException(i, i.getClass().getSimpleName());
        validator.validate(i, result, Default.class);
        if(result.hasErrors()){
            ValidationException vdEx = new ValidationException(List.of(result));
            throw vdEx;
        }
        preProcess.doPreCreate(this, i);
        T o = converter.toDomain(i);
        postProcess.doPostCreate(this, i, o);
        if(Objects.nonNull(o)){
            o.setItemCount(i.getItemCount());
        }
        return o;
    }
}
