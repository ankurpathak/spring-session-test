package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.batch.item.processor.DomainItemProcessor;
import com.github.ankurpathak.api.batch.item.processor.listener.DomainItemProcessListener;
import com.github.ankurpathak.api.batch.item.reader.DomainItemReader;
import com.github.ankurpathak.api.batch.item.reader.listener.DomainItemReadListener;
import com.github.ankurpathak.api.batch.item.writer.DomainItemWriter;
import com.github.ankurpathak.api.batch.item.writer.listener.DomainItemWriteListener;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.converter.ProductConverters;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

@Configuration
public class ProductTaskCsvConfig extends AbstractDomainCsvTaskConfig<Product, String, ProductDto> {

    @Autowired
    private GenericApplicationContext applicationContext;

    public interface ProductTask{
        String CSV_PRODUCT_STEP = String.format("%s_STEP", Task.TaskType.CSV_PRODUCT);
        String CSV_PRODUCT_READER = String.format("%s_READER", Task.TaskType.CSV_PRODUCT);
        String CSV_PRODUCT_READ_LISTNER = String.format("%s_READ_LISTNER", Task.TaskType.CSV_PRODUCT);
        String CSV_PRODUCT_WRITER = String.format("%s_WRITER", Task.TaskType.CSV_PRODUCT);
        String CSV_PRODUCT_WRITE_LISTENER = String.format("%s_WRITE_LISTENER", Task.TaskType.CSV_PRODUCT);
        String CSV_PRODUCT_PROCESSOR = String.format("%s_PROCESSOR", Task.TaskType.CSV_PRODUCT);
        String CSV_PRODUCT_PROCESS_LISTENER = String.format("%s_PROCESS_LISTENER", Task.TaskType.CSV_PRODUCT);
    }

    @PostConstruct
    public void init() throws Exception{
        DomainItemReader<ProductDto, Product, String> itemReader = itemReader();
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_READER, DomainItemReader.class, () -> itemReader);
        DomainItemReadListener<ProductDto, String, Product> itemReadListener = itemReadListener();
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_READ_LISTNER, DomainItemReadListener.class, () -> itemReadListener);
        DomainItemWriter<Product, String> itemWriter = itemWriter();
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_WRITER, DomainItemWriter.class, () -> itemWriter);
        DomainItemProcessor<ProductDto, String, Product> itemProcessor = itemProcessor();
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_PROCESSOR, DomainItemProcessor.class, () -> itemProcessor);
        DomainItemProcessListener<ProductDto, String, Product> itemProcessListener = itemProcessListener();
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_PROCESS_LISTENER, DomainItemProcessListener.class, () -> itemProcessListener);
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_PROCESS_LISTENER, DomainItemProcessListener.class, () -> itemProcessListener);
        DomainItemWriteListener<Product, String> itemWriteListener = itemWriteListener();
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_WRITE_LISTENER, DomainItemWriteListener.class, () -> itemWriteListener);
        Step step = step(ProductTask.CSV_PRODUCT_STEP, itemReader, itemProcessor, itemWriter, itemReadListener, itemProcessListener, itemWriteListener);
        applicationContext.registerBean(ProductTask.CSV_PRODUCT_STEP, Step.class, () -> step);
        Job job = job(Task.TaskType.CSV_PRODUCT, step);
        applicationContext.registerBean(Task.TaskType.CSV_PRODUCT, Job.class, () -> job);
    }

    @Override
    protected Class<ProductDto> getDtoType() {
        return ProductDto.class;
    }

    @Override
    protected Class<Product> getType() {
        return Product.class;
    }

    @Override
    protected IToDomain<Product, String, ProductDto> getConverter() {
        return ProductConverters.createOne;
    }
}
