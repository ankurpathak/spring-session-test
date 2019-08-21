package com.github.ankurpathak.api.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class BatchConfig implements BatchConfigurer {
    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Override
    public JobRepository getJobRepository() {
        return jobRepository;
    }
    @Override
    public PlatformTransactionManager getTransactionManager() {
        return new MongoTransactionManager(mongoDbFactory);
    }
    @Override
    public JobLauncher getJobLauncher()  {
        return jobLauncher;
    }
    @Override
    public JobExplorer getJobExplorer() {
        return jobExplorer;
    }
}
