package com.github.ankurpathak.api.batch.item.writer.builder;

import com.github.ankurpathak.api.batch.item.writer.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;


public class MongoItemWriterBuilder<T> {

    private MongoOperations template;
    private Class<T> type;


    public MongoItemWriterBuilder<T> template(MongoOperations template) {
        this.template = template;
        return this;
    }


    public MongoItemWriterBuilder<T> type(Class<T> type) {
        this.type = type;
        return this;
    }

    public MongoItemWriter<T> build() {
        require(this.template, notNullValue());
        require(this.type, notNullValue());
        MongoItemWriter<T> writer = new MongoItemWriter<>();
        writer.setTemplate(this.template);
        writer.setType(this.type);
        return writer;
    }

}
