package com.github.ankurpathak.api.batch.item.processor.listener;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;

import java.io.Serializable;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;


public class DomainItemProcessListener<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID>> implements ItemProcessListener<Tdto,  T> {

    private Task task;
    private User user;
    private Business business;
    private FileContext file;
    private final Class<T> tClass;
    private final Class<Tdto> tdtoClass;

    public DomainItemProcessListener(Class<T> tClass, Class<Tdto> tdtoClass) {
        this.tClass = tClass;
        this.tdtoClass = tdtoClass;
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

    @Override
    public void beforeProcess(Tdto item) { }

    @Override
    public void afterProcess(Tdto item, T result) { }

    @Override
    public void onProcessError(Tdto item, Exception e) { }
}
