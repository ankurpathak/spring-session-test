package com.github.ankurpathak.api.config;

import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.mongodb.MongoExecutionContextDao;
import org.springframework.batch.mongodb.MongoJobExecutionDao;
import org.springframework.batch.mongodb.MongoJobInstanceDao;
import org.springframework.batch.mongodb.MongoStepExecutionDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class BatchDaoConfig {

    private MongoTemplate mongoTemplate;

    public BatchDaoConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public ExecutionContextDao createExecutionContextDao() {
        MongoExecutionContextDao dao = new MongoExecutionContextDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public JobExecutionDao createJobExecutionDao() {
        MongoJobExecutionDao dao = new MongoJobExecutionDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public JobInstanceDao createJobInstanceDao() {
        MongoJobInstanceDao dao = new MongoJobInstanceDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public StepExecutionDao createStepExecutionDao() {
        MongoStepExecutionDao dao = new MongoStepExecutionDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }
}
