package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.batch.item.reader.OpenCsvItemReader;
import com.github.ankurpathak.api.domain.converter.IToDomain;
import com.github.ankurpathak.api.domain.converter.ProductConverters;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ProductTaskCsvConfig extends AbstractDomainCsvTaskConfig<Product, String, ProductDto> {
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

    @Bean(name = "productCsvTask")
    protected Job job(@Qualifier("productCsvStep") Step step) {
        return super.job(Task.TaskType.CSV_PRODUCT.name(), step);
    }

    @Bean(name = "productCsvStep")
    protected Step step(@Qualifier("productCsvReader") ItemReader<ProductDto> itemReader, @Qualifier("productCsvProcessor") ItemProcessor<ProductDto, Product> itemProcessor, @Qualifier("productCsvWriter") ItemWriter<Product> itemWriter) throws Exception {
        return super.step(Task.TaskType.CSV_PRODUCT.name(), itemReader, itemProcessor, itemWriter);
    }

    @Override
    @Bean(name = "productCsvReader")
    @StepScope
    @Lazy
    protected OpenCsvItemReader<ProductDto> itemReader(@Value("#{jobParameters['task']}") String taskJson) throws Exception {
        return super.itemReader(taskJson);
    }

    @Override
    @Bean(name = "productCsvProcessor")
    @StepScope
    @Lazy
    protected ItemProcessor<ProductDto, Product> itemProcessor(@Value("#{jobParameters['task']}") String taskJson) throws Exception {
        return super.itemProcessor(taskJson);
    }

    @Override
    @Bean(name = "productCsvWriter")
    @StepScope
    @Lazy
    protected ItemWriter<Product> itemWriter(@Value("#{jobParameters['task']}") String taskJson) throws Exception {
        return super.itemWriter(taskJson);
    }
}
