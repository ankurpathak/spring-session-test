package com.github.ankurpathak.api.batch.item.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

public class MongoItemWriter<T> implements ItemWriter<T> {

    private MongoOperations template;
    private Class<T> type;

    public void setType(Class<T> type) {
        this.type = type;
    }

    public void setTemplate(MongoOperations template) {
        this.template = template;
    }


    @Override
    public void write(List<? extends T> items) throws Exception {
        template.insertAll(items);
    }

}

