package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.util.PropertyUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableConfigurationProperties({MongoProperties.class})
public class MongoConfig extends AbstractMongoConfiguration {
    private static Logger log = LoggerFactory.getLogger(MongoConfig.class);
    @Autowired
    private MongoProperties properties;

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }


    @Override
    @Bean
    public MongoClient mongoClient() {
        MongoClientOptions.Builder mongoClientOptionsBuilder = MongoClientOptions.builder()
                .writeConcern(WriteConcern.ACKNOWLEDGED)
                .socketKeepAlive(true);
        return new MongoClient(new MongoClientURI(properties.getUri(), mongoClientOptionsBuilder));
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
