package com.github.ankurpathak.api.batch.item.writer.listener;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;


public class DomainItemWriteListener<T extends Domain<ID>, ID extends Serializable> implements ItemWriteListener<T> {

    private Task task;
    private User user;
    private Business business;
    private FileContext file;
    private final Class<T> tClass;

    public DomainItemWriteListener(Class<T> tClass) {
        this.tClass = tClass;
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
    public void beforeWrite(List<? extends T> items) {

    }

    @Override
    public void afterWrite(List<? extends T> items) {

    }

    @Override
    public void onWriteError(Exception exception, List<? extends T> items) {

    }
}
