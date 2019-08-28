package com.github.ankurpathak.api.config;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.mongodb.MongoJobRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;


@Configuration
public class BatchConfig implements BatchConfigurer {

    private final MongoTemplate template;
    private final ExecutionContextDao executionContextDao;
    private final JobExecutionDao jobExecutionDao;
    private final JobInstanceDao jobInstanceDao;
    private final StepExecutionDao stepExecutionDao;

    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;
    private PlatformTransactionManager transactionManager;

    public BatchConfig(MongoTemplate template, ExecutionContextDao executionContextDao, JobExecutionDao jobExecutionDao, JobInstanceDao jobInstanceDao, StepExecutionDao stepExecutionDao) {
        this.template = template;
        this.executionContextDao = executionContextDao;
        this.jobExecutionDao = jobExecutionDao;
        this.jobInstanceDao = jobInstanceDao;
        this.stepExecutionDao = stepExecutionDao;
    }


    @Override
    public JobRepository getJobRepository() {
        return jobRepository;
    }
    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }
    @Override
    public JobLauncher getJobLauncher()  {
        return jobLauncher;
    }
    @Override
    public JobExplorer getJobExplorer() {
        return jobExplorer;
    }


    @PostConstruct
    public void initialize() {
        try {
            this.transactionManager = createTransactionManager();
            MongoJobRepositoryFactoryBean mongoJobRepositoryFactoryBean = createJobRepository();
            this.jobRepository = mongoJobRepositoryFactoryBean.getObject();
            this.jobExplorer = createjobExplorer();
            this.jobLauncher = createJobLauncher();
        } catch (Exception e) {
            throw new BatchConfigurationException(e);
        }
    }

    public PlatformTransactionManager createTransactionManager(){
        return new MongoTransactionManager(template.getMongoDbFactory());
    }

    public MongoJobRepositoryFactoryBean createJobRepository() throws Exception {
        MongoJobRepositoryFactoryBean mongoJobRepositoryFactoryBean = new MongoJobRepositoryFactoryBean();
        mongoJobRepositoryFactoryBean.setExecutionContextDao(this.executionContextDao);
        mongoJobRepositoryFactoryBean.setJobExecutionDao(this.jobExecutionDao);
        mongoJobRepositoryFactoryBean.setJobInstanceDao(this.jobInstanceDao);
        mongoJobRepositoryFactoryBean.setStepExecutionDao(this.stepExecutionDao);
        mongoJobRepositoryFactoryBean.setTransactionManager(this.transactionManager);
        mongoJobRepositoryFactoryBean.afterPropertiesSet();
        return mongoJobRepositoryFactoryBean;
    }

    protected JobExplorer createjobExplorer() {
        return new SimpleJobExplorer(
                this.jobInstanceDao,
                this.jobExecutionDao,
                this.stepExecutionDao,
                this.executionContextDao
        );
    }

    protected JobLauncher createJobLauncher() throws Exception{
        SimpleJobLauncher jobLauncher =new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
