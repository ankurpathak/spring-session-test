package com.github.ankurpathak.api.batch.item.writer;

import com.github.ankurpathak.api.batch.item.writer.builder.MongoItemWriterBuilder;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

public class DomainItemWriter<T extends Domain<ID>, ID extends Serializable> implements ItemWriter<T> {

    private ItemWriter<T> delegate;
    private final MongoTemplate mongoTemplate;
    private final Class<T> tClass;

    public DomainItemWriter(MongoTemplate mongoTemplate, Class<T> tClass) {
        this.mongoTemplate = mongoTemplate;
        this.tClass = tClass;
    }


    @BeforeStep
    public void beforeStep(StepExecution execution) {
        delegate = new MongoItemWriterBuilder<T>().template(mongoTemplate).type(tClass).build();
    }


    @Override
    public void write(List<? extends T> items) throws Exception {
        delegate.write(items);
    }
}
