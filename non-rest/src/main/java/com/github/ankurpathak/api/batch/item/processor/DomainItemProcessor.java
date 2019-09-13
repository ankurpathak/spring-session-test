package com.github.ankurpathak.api.batch.item.processor;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.exception.ValidationException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

public class DomainItemProcessor<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID> > implements ItemProcessor<Tdto, T> {

    private final LocalValidatorFactoryBean validator;
    private final IToDomain<T, ID, Tdto> converter;
    private final Class<T> tClass;
    private final Class<Tdto> dDtoClass;

    private Task task;
    private User user;
    private Business business;
    private FileContext file;

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

    @BeforeStep
    public void beforeStep(StepExecution execution) {
        this.task = PrimitiveUtils.cast(execution.getJobExecution().getExecutionContext().get("task"), Task.class);
        ensure(this.task, notNullValue());
        this.user = PrimitiveUtils.cast(execution.getJobExecution().getExecutionContext().get("user"), User.class);
        ensure(this.user, notNullValue());
        this.business = PrimitiveUtils.cast(execution.getJobExecution().getExecutionContext().get("business"), Business.class);
        ensure(this.business, notNullValue());
        this.file = PrimitiveUtils.cast(execution.getJobExecution().getExecutionContext().get("file"), FileContext.class);
        ensure(this.file, notNullValue());
    }
}
