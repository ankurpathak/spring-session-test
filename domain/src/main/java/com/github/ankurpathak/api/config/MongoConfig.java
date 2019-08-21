package com.github.ankurpathak.api.config;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoAuditing(
        auditorAwareRef = "userIdAuditorAware",
        dateTimeProviderRef = "instantDateTimeProvider"
)
@EnableConfigurationProperties({MongoProperties.class})
public class MongoConfig extends AbstractMongoConfiguration {


    private static Logger log = LoggerFactory.getLogger(MongoConfig.class);

    @Autowired
    private MongoProperties properties;

    @Autowired
    private MongoClient mongoClient;


    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }


    @Override
    public MongoClient mongoClient() {
        return mongoClient;
    }

    @Override
    @Bean
    public MongoDbFactory mongoDbFactory() {
        return super.mongoDbFactory();
    }


    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoDbFactory());
    }

}
