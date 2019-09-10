package com.github.ankurpathak.api.batch.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.batch.item.processor.DomainItemProcessor;
import com.github.ankurpathak.api.batch.item.processor.listener.DomainItemProcessListener;
import com.github.ankurpathak.api.batch.item.reader.DomainItemReader;
import com.github.ankurpathak.api.batch.item.writer.DomainItemWriter;
import com.github.ankurpathak.api.batch.item.writer.listener.DomainItemWriteListener;
import com.github.ankurpathak.api.batch.task.listener.TaskStatusListener;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.ITaskService;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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




    protected Job job(String name, Step step){
        return this.jobBuilderFactory.get(name)
                .incrementer(new RunIdIncrementer())
                .listener(new TaskStatusListener(taskService, userDetailsService, businessService, fileService))
                .start(step).build();
    }

    abstract protected Class<Tdto> getDtoType();

    abstract protected Class<T> getType();

    abstract protected IToDomain<T,ID,Tdto> getConverter();




    protected Step step(String name, ItemReader<Tdto> itemReader, ItemProcessor<Tdto, T> itemProcessor, ItemWriter<T> itemWriter, ItemProcessListener<Tdto, T> itemProcessListener) throws Exception{
        return this.stepBuilderFactory.get(name)
                .<Tdto, T>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .listener(itemProcessListener)
                .writer(itemWriter)
                .build();
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
        return new DomainItemProcessListener<>(getType(), getDtoType());
    }

    protected DomainItemWriteListener<T,ID> itemWriteListener(){
        return new DomainItemWriteListener<>(getType());
    }
}
