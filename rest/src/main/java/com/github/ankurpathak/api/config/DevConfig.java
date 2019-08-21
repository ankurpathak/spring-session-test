package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.annotation.Dev;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Dev
@EnableConfigurationProperties({MongoProperties.class})
public class DevConfig {

    @Autowired
    private MongoProperties properties;

    @Bean
    public MongoClient mongoClient(){
        MongoClientOptions.Builder mongoClientOptionsBuilder = MongoClientOptions.builder()
                .writeConcern(WriteConcern.ACKNOWLEDGED)
                .socketKeepAlive(true);
        return new MongoClient(new MongoClientURI(properties.getUri(), mongoClientOptionsBuilder));
    }
}
