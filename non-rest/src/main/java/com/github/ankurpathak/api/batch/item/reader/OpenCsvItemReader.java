package com.github.ankurpathak.api.batch.item.reader;

import com.github.ankurpathak.api.util.OpenCsvBeanReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.core.io.Resource;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class OpenCsvItemReader<T> extends ItemStreamSupport implements ItemReader<T>  {
    private final Resource resource;
    private final Class<T> tClass;
    private OpenCsvBeanReader<T> reader;

    public OpenCsvItemReader(Resource resource, Class<T> tClass) {
        require(resource, notNullValue());
        require(tClass, notNullValue());
        this.resource = resource;
        this.tClass = tClass;

    }

    @Override
    public T read() throws Exception{
        return this.reader.readLine().orElse(null);
    }

    @Override
    public void open(ExecutionContext executionContext) {
        this.reader = new OpenCsvBeanReader<>(resource, tClass);
    }

    @Override
    public void close() {
        this.reader.shutDown();
    }
}
