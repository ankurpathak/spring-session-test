package com.github.ankurpathak.api.batch.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controllor.dto.ProductDto;
import com.github.ankurpathak.api.service.IFileService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ProductCsvTaskConfig {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IFileService fileService;

    @Bean
    public Job productCsvTask(JobBuilderFactory jobBuilderFactory, @Qualifier("productCsvStep") Step productCsvStep){
        return jobBuilderFactory.get(Task.TaskType.CSV_PRODUCT.name())
                .incrementer(new RunIdIncrementer())
                .start((Step)null)
                .build();
    }

    @Bean
    @StepScope
    public Step productCsvStep(StepBuilderFactory stepBuilderFactory, @Value("#{jobExecutionContext['task']}") Task task){
        return stepBuilderFactory.get(Task.TaskType.CSV_PRODUCT.name())
                .<ProductDto, Product>chunk(1000)
                .reader(createProductItemReader(task))
                .processor(createPrductItemProcessor())
                .writer(createProductItemWriter())
                .build();
    }


    public ItemReader<ProductDto> createProductItemReader(Task task){
        return null;
    }


    public ItemProcessor<ProductDto, Product> createPrductItemProcessor(){
        return null;
    }


    public ItemWriter<Product> createProductItemWriter(){
        return null;
    }


}
