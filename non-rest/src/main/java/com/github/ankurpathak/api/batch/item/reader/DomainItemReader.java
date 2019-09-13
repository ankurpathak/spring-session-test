package com.github.ankurpathak.api.batch.item.reader;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.core.io.FileSystemResource;

import java.io.Serializable;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

public class DomainItemReader<Tdto extends DomainDto<T, ID>, T extends Domain<ID>, ID extends Serializable> extends ItemStreamSupport implements ItemReader<Tdto> {

    private ItemReader<Tdto> delegate;
    private Task task;
    private User user;
    private Business business;
    private FileContext file;
    private final Class<Tdto> tDtoClass;

    public DomainItemReader(Class<Tdto> tDtoClass) {
        this.tDtoClass = tDtoClass;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        if(delegate instanceof ItemStreamSupport){
            ((ItemStreamSupport)delegate).open(executionContext);
        }

    }

    @Override
    public void close() {
        if(delegate instanceof ItemStreamSupport){
            ((ItemStreamSupport)delegate).close();
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
        if(delegate instanceof ItemStreamSupport){
            ((ItemStreamSupport)delegate).update(executionContext);
        }
    }

    @Override
    public Tdto read() throws Exception {
        return delegate.read();
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
        delegate = new OpenCsvItemReader<>(new FileSystemResource(file.getPath()), tDtoClass);
    }
}
