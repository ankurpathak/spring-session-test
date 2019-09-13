package com.github.ankurpathak.api.config.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.Test;
import com.github.ankurpathak.api.domain.mongo.codec.BigDecimalCodec;
import com.github.ankurpathak.api.domain.mongo.codec.BigIntegerCodec;
import com.github.ankurpathak.api.domain.mongo.codec.PojoCodecProvider;
import com.github.ankurpathak.api.testcontainer.mongo.MongoDbContainer;
import com.github.ankurpathak.api.util.LogUtil;
import com.mongodb.DocumentToDBRefTransformer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.bson.Document;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;

@Configuration
@Test
public class MongoConfig {

    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);


    private final ConfigurableEnvironment environment;
    private final MongoProperties properties;
    private final ObjectMapper objectMapper;

    public MongoConfig(MongoProperties properties, ConfigurableEnvironment environment, ObjectMapper objectMapper) {
        this.properties = properties;
        this.environment = environment;
        this.objectMapper = objectMapper;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public MongoDbContainer mongo() {
        MongoDbContainer mongo = new MongoDbContainer()
                .withCommand("--replSet rs");
        return mongo;
    }


    @Bean(destroyMethod = "close")
    public MongoClient mongoClient(MongoDbContainer mongo) {
        setMongoUri(mongo);
        MongoClientFactory factory = new MongoClientFactory(properties, environment);
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry())
                .socketKeepAlive(true).build();
        return factory.createMongoClient(options);
    }

    public void setMongoUri(MongoDbContainer mongo){
        MutablePropertySources sources = environment.getPropertySources();
        String mongoUri = String.format("mongodb://%s:%d/test", mongo.getContainerIpAddress(), mongo.getPort());
        LogUtil.logValue(log, "mongoUri", mongoUri);
        MapPropertySource mapProperties = new MapPropertySource("mongo", Map.of("spring.data.mongodb.uri", mongoUri));
        sources.addFirst(mapProperties);
        properties.setUri(mongoUri);
    }

    private CodecRegistry codecRegistry() {
        CodecRegistry extendedDefaultCodecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(new BigIntegerCodec(), new BigDecimalCodec()),
                MongoClient.getDefaultCodecRegistry());

        CodecProvider documentCodecProvider = new DocumentCodecProvider(new DocumentToDBRefTransformer());

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(new PojoCodecProvider(documentCodecProvider.get(Document.class, extendedDefaultCodecRegistry), objectMapper));

        return CodecRegistries.fromRegistries(
                extendedDefaultCodecRegistry,
                pojoCodecRegistry
        );
    }


}
