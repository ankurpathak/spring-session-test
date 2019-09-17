package com.github.ankurpathak.api.batch.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.batch.item.processor.DomainItemProcessor;
import com.github.ankurpathak.api.batch.item.processor.listener.DomainItemProcessListener;
import com.github.ankurpathak.api.batch.item.reader.DomainItemReader;
import com.github.ankurpathak.api.batch.item.reader.listener.DomainItemReadListener;
import com.github.ankurpathak.api.batch.item.writer.DomainItemWriter;
import com.github.ankurpathak.api.batch.item.writer.listener.DomainItemWriteListener;
import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.batch.task.exception.handler.impl.*;
import com.github.ankurpathak.api.batch.task.listener.TaskStatusListener;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.ITaskService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.Serializable;
import java.util.List;


public abstract class AbstractDomainCsvTaskConfig<T extends Domain<ID>, ID extends Serializable, Tdto extends DomainDto<T,ID>> {
    @Autowired
    protected JobBuilderFactory jobBuilderFactory;
    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Autowired
    protected IFileService fileService;
    @Autowired
    protected ITaskService taskService;
    @Autowired
    protected CustomUserDetailsService userDetailsService;
    @Autowired
    protected IBusinessService businessService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected LocalValidatorFactoryBean validator;
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected IMessageService messageService;

    @Autowired
    protected IExceptionHandler<?> exceptionHandler;


    protected Job job(String name, Step step){
        return this.jobBuilderFactory.get(name)
                .incrementer(new RunIdIncrementer())
                .listener(new TaskStatusListener(taskService, userDetailsService, businessService, fileService, messageService))
                .start(step).build();
    }

    abstract protected Class<Tdto> getDtoType();

    abstract protected Class<T> getType();

    abstract protected IToDomain<T,ID,Tdto> getConverter();




    protected Step step(String name, ItemReader<Tdto> itemReader, ItemProcessor<Tdto, T> itemProcessor, ItemWriter<T> itemWriter, ItemReadListener<Tdto> itemReadListener, ItemProcessListener<Tdto, T> itemProcessListener, ItemWriteListener<T> itemWriteListener) throws Exception{
        return this.stepBuilderFactory.get(name)
                .<Tdto, T>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .listener(itemReadListener)
                .listener(itemProcessListener)
                .listener(itemWriteListener)
                .writer(itemWriter)
                .build();
    }


    protected IExceptionHandler<?> exceptionHandler(List<IExceptionHandler<?>> exceptionHandlers){
        return new CompositeExceptionHandler(exceptionHandlers);
    }

    protected DomainItemReader<Tdto, T, ID> itemReader()  {
        return new DomainItemReader<>(getDtoType());
    }



    protected DomainItemProcessor<Tdto,ID, T> itemProcessor(){
        return new DomainItemProcessor<>(validator, getConverter(), getType(), getDtoType());
    }


    protected DomainItemWriter<T, ID> itemWriter() {
       return new DomainItemWriter<>(mongoTemplate, getType());
    }

    protected DomainItemProcessListener<Tdto,ID, T> itemProcessListener(){
        return new DomainItemProcessListener<>(getType(), getDtoType(), exceptionHandler);
    }

    protected DomainItemReadListener<Tdto,ID, T> itemReadListener(){
        return new DomainItemReadListener<>(getDtoType(), taskService, exceptionHandler);
    }

    protected DomainItemWriteListener<T,ID, Tdto> itemWriteListener(){
        return new DomainItemWriteListener<>(getType(), getDtoType(), exceptionHandler);
    }
}
