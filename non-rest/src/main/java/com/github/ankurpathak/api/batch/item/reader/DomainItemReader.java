package com.github.ankurpathak.api.batch.item.reader;

import com.github.ankurpathak.api.batch.task.CsvTaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.core.io.FileSystemResource;

import java.io.Serializable;

public class DomainItemReader<Tdto extends DomainDto<T, ID>, T extends Domain<ID>, ID extends Serializable> extends ItemStreamSupport implements ItemReader<Tdto> {

    private ItemReader<Tdto> delegate;
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
        if(ItemStreamSupport.class.isInstance(delegate)){
            ((ItemStreamSupport)delegate).update(executionContext);
        }
    }

    @Override
    public Tdto read() throws Exception {
        return delegate.read();
    }

    @BeforeStep
    public void beforeStep(StepExecution execution) {
        TaskContextHolder.getContext()
                .filter(CsvTaskContext.class::isInstance)
                .map(CsvTaskContext.class::cast)
                .flatMap(CsvTaskContext::getFile)
                .ifPresent( file -> {
                    this.delegate = new OpenCsvItemReader<>(new FileSystemResource(file.getPath()), tDtoClass);
                });
    }
}
