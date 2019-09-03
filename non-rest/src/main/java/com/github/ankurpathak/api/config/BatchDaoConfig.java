package com.github.ankurpathak.api.config;

import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.mongodb.MongoExecutionContextDao;
import org.springframework.batch.mongodb.MongoJobExecutionDao;
import org.springframework.batch.mongodb.MongoJobInstanceDao;
import org.springframework.batch.mongodb.MongoStepExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class BatchDaoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Bean
    public ExecutionContextDao createExecutionContextDao() {
        MongoExecutionContextDao dao = new MongoExecutionContextDao();
        dao.setMongoTemplate(mongoTemplate);
        return dao;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor() {
            @Override
            protected void doExecute(Runnable task) {
                //gets the jobExecution of the configuration thread
                var jobExecution = JobSynchronizationManager.getContext().getJobExecution();
                super.doExecute(new Runnable() {

                    @Override
                    public void run() {
                        JobSynchronizationManager.register(jobExecution);

                        try {
                            task.run();
                        } finally {
                            JobSynchronizationManager.release();
                        }
                    }
                });
            }
        };
    }


    @Bean
    public StepScope stepScope() {
        final StepScope stepScope = new StepScope();
        stepScope.setAutoProxy(true);
        return stepScope;
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
