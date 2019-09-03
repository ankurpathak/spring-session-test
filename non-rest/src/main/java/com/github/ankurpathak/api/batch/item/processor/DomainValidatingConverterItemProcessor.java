package com.github.ankurpathak.api.batch.item.processor;

import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.exception.ValidationException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.Serializable;
import java.util.List;

public class DomainValidatingConverterItemProcessor<I extends DomainDto<O, ID>, ID extends Serializable, O extends Domain<ID> > implements ItemProcessor<I, O> {

    private final LocalValidatorFactoryBean validator;
    private final IToDomain<O, ID,I> converter;
    private final Business business;

    public DomainValidatingConverterItemProcessor(LocalValidatorFactoryBean validator, IToDomain<O, ID, I> converter, Business business) {
        this.validator = validator;
        this.converter = converter;
        this.business = business;
    }

    @Override
    public O process(I i) throws Exception {
        BindException result = new BindException(i, i.getClass().getSimpleName());
        validator.validate(i, result);
        if(result.hasErrors()){
            ValidationException vdEx = new ValidationException(List.of(result), ApiCode.VALIDATION, ApiMessages.VALIDATION);
            throw vdEx;
        }
        return converter.toDomain(i);
    }

}
