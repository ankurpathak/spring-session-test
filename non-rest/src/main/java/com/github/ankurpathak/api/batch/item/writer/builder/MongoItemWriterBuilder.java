package com.github.ankurpathak.api.batch.item.writer.builder;
import com.github.ankurpathak.api.batch.item.writer.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;


public class MongoItemWriterBuilder<T> {

    private MongoOperations template;

    private String collection;

    private boolean delete = false;


    public MongoItemWriterBuilder<T> delete(boolean delete) {
        this.delete = delete;
        return this;
    }


    public MongoItemWriterBuilder<T> template(MongoOperations template) {
        this.template = template;

        return this;
    }


    public MongoItemWriterBuilder<T> collection(String collection) {
        this.collection = collection;
        return this;
    }

    public MongoItemWriter<T> build() {
        Assert.notNull(this.template, "template is required.");
        MongoItemWriter<T> writer = new MongoItemWriter<>();
        writer.setTemplate(this.template);
        writer.setDelete(this.delete);
        writer.setCollection(this.collection);
        return writer;
    }

}
