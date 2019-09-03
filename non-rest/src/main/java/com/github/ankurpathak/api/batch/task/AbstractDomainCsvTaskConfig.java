package com.github.ankurpathak.api.batch.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.batch.item.processor.DomainValidatingConverterItemProcessor;
import com.github.ankurpathak.api.batch.item.reader.OpenCsvItemReader;
import com.github.ankurpathak.api.batch.item.writer.builder.MongoItemWriterBuilder;
import com.github.ankurpathak.api.batch.task.listener.TaskStatusListener;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.model.*;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.ITaskService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.Serializable;


public abstract class AbstractDomainCsvTaskConfig<T extends Domain<ID>, ID extends Serializable, Tdto extends DomainDto<T,ID>> {

    @Autowired
    protected JobBuilderFactory jobBuilderFactory;
    @Autowired
    protected StepBuilderFactory stepBuilderFactory;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected IFileService fileService;
    @Autowired
    protected LocalValidatorFactoryBean validator;
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected ITaskService taskService;


    protected Job job(String name, Step step){
        return this.jobBuilderFactory.get(name)
                .incrementer(new RunIdIncrementer())
                .listener(new TaskStatusListener(taskService, objectMapper))
                .start(step).build();
    }

    abstract protected Class<Tdto> getDtoType();

    abstract protected Class<T> getType();

    abstract protected IToDomain<T,ID,Tdto> getConverter();




    protected Step step(String name, ItemReader<Tdto> itemReader, ItemProcessor<Tdto, T> itemProcessor, ItemWriter<T> itemWriter) throws Exception{
        return this.stepBuilderFactory.get(name)
                .<Tdto, T>chunk(10)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }



    protected OpenCsvItemReader<Tdto> itemReader(String taskJson) throws Exception {
        Task task = objectMapper.readValue(taskJson, Task.class);
        String fileId = MapUtils.getString(task.getRequest(), "fileId");
        return fileService.findById(fileId)
                .map(con -> new OpenCsvItemReader<Tdto>(new FileSystemResource(con.getPath()), getDtoType()))
                .get();
    }



    protected ItemProcessor<Tdto, T> itemProcessor(String taskJson) throws Exception{
        Task task = objectMapper.readValue(taskJson, Task.class);
        User user = objectMapper.convertValue(MapUtils.getMap(task.getRequest(), "user"), VUserBusiness.class);
        Business business = objectMapper.convertValue(MapUtils.getMap(task.getRequest(), "business"), Business.class);
        return new DomainValidatingConverterItemProcessor<Tdto, ID, T>(validator, getConverter() ,business);
    }


    protected ItemWriter<T> itemWriter(String taskJson)throws Exception{
        Task task = objectMapper.readValue(taskJson, Task.class);
        return new MongoItemWriterBuilder<T>()
                .template(mongoTemplate)
                .build();
    }
}
