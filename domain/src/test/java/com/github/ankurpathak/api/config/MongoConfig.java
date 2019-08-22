package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.annotation.Test;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
@Test
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig extends AbstractMongoConfiguration {
    @Autowired
    private MongoProperties properties;

    @Bean(initMethod = "start", destroyMethod = "close")
    public MongoDbContainer container(){
        MongoDbContainer mongo = new MongoDbContainer()
                .withCommand("--replSet rs");
        return mongo;
    }

    @Override
    public MongoClient mongoClient() {
        MongoDbContainer mongo = container();
        MongoClientOptions.Builder mongoClientOptionsBuilder = MongoClientOptions.builder()
                .writeConcern(WriteConcern.ACKNOWLEDGED)
                .socketKeepAlive(true);
        return new MongoClient(new MongoClientURI(String.format("mongodb://%s:%d/test", mongo.getContainerIpAddress(), mongo.getPort()), mongoClientOptionsBuilder));
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }

    @Bean
    public GridFsTemplate gridFsTemplate(MongoDbFactory factory, MongoConverter converter){
        return new GridFsTemplate(factory, converter);
    }
}
