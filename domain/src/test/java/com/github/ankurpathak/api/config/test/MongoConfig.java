package com.github.ankurpathak.api.config.test;

import com.github.ankurpathak.api.annotation.Test;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.api.util.LogUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.data.mongodb.core.WriteConcernResolver;

import java.util.Map;

@Configuration
@Test
public class MongoConfig  {

    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);


    private final ConfigurableEnvironment environment;
    private final MongoProperties properties;

    public MongoConfig(MongoProperties properties, ConfigurableEnvironment environment) {
        this.properties = properties;
        this.environment = environment;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public MongoDbContainer mongo(){
        MongoDbContainer mongo = new MongoDbContainer()
                .withCommand("--replSet rs");
        return mongo;
    }



    @Bean(destroyMethod = "close")
    public MongoClient mongoClient(MongoDbContainer mongo) {
        LogUtil.logValue(log, "port", mongo.getPort().toString());
        MutablePropertySources sources = environment.getPropertySources();
        String mongoUri = String.format("mongodb://%s:%d/test", mongo.getContainerIpAddress(), mongo.getPort());
        MapPropertySource mapProperties = new MapPropertySource("mongo",Map.of("spring.data.mongodb.uri", mongoUri ) );
        sources.addFirst(mapProperties);
        properties.setUri(mongoUri);
        MongoClientFactory factory = new MongoClientFactory(properties, environment);
        MongoClientOptions options = MongoClientOptions.builder()
                .socketKeepAlive(true).build();
        return factory.createMongoClient(options);
    }

    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> {
            return WriteConcern.ACKNOWLEDGED;
        };
    }

    
}
