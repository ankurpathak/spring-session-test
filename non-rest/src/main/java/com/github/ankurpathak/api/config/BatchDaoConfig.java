package com.github.ankurpathak.api.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchDaoConfig {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public ExecutionContextDao executionContextDao() {
        MongoExecutionContextDao dao = new MongoExecutionContextDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public JobExecutionDao jobExecutionDao() {
        MongoJobExecutionDao dao = new MongoJobExecutionDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public JobInstanceDao jobInstanceDao() {
        MongoJobInstanceDao dao = new MongoJobInstanceDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public StepExecutionDao stepExecutionDao() {
        MongoStepExecutionDao dao = new MongoStepExecutionDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(MongoDbFactory mongoDbFactory) {
        return new MongoTransactionManager(mongoDbFactory);
    }

    @Bean
    public MongoJobRepositoryFactoryBean jobRepository(PlatformTransactionManager platformTransactionManager, JobInstanceDao jobInstanceDao, JobExecutionDao jobExecutionDao, StepExecutionDao stepExecutionDao, ExecutionContextDao executionContextDao) {
        MongoJobRepositoryFactoryBean mongoJobRepositoryFactoryBean = new MongoJobRepositoryFactoryBean();
        mongoJobRepositoryFactoryBean.setExecutionContextDao(executionContextDao);
        mongoJobRepositoryFactoryBean.setJobExecutionDao(jobExecutionDao);
        mongoJobRepositoryFactoryBean.setJobInstanceDao(jobInstanceDao);
        mongoJobRepositoryFactoryBean.setStepExecutionDao(stepExecutionDao);
        mongoJobRepositoryFactoryBean.setTransactionManager(platformTransactionManager);
        return mongoJobRepositoryFactoryBean;
    }

    @Bean
    public JobExplorer jobExplorer(JobInstanceDao jobInstanceDao, JobExecutionDao jobExecutionDao, StepExecutionDao stepExecutionDao, ExecutionContextDao executionContextDao) {
        return new SimpleJobExplorer(
                jobInstanceDao,
                jobExecutionDao,
                stepExecutionDao,
                executionContextDao
        );
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception{
        SimpleJobLauncher jobLauncher =new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }


    @Bean
    public JobOperator jobOperator(JobExplorer jobExplorer, JobLauncher jobLauncher, JobRepository jobRepository, JobRegistry jobRegistry){
        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        return jobOperator;
    }

    @Bean
    public JobRegistry jobRegistry(){
        return new MapJobRegistry();
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory(JobRepository jobRepository){
        return new JobBuilderFactory(jobRepository);
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilderFactory(jobRepository, transactionManager);
    }
}
