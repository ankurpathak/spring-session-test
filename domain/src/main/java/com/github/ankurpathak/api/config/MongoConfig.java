package com.github.ankurpathak.api.config;

import com.mongodb.WriteConcern;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Configuration
@EnableMongoAuditing(
        auditorAwareRef = "userIdAuditorAware",
        dateTimeProviderRef = "instantDateTimeProvider"
)
public class MongoConfig  {

    @Bean
    @ConditionalOnMissingClass("org.springframework.batch.core.repository.JobRepository")
    public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }


    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> {
            return WriteConcern.ACKNOWLEDGED;
        };
    }


    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new Converter<LocalDate, Instant>() {
                    @Override
                    public Instant convert(LocalDate source) {
                        return source.atStartOfDay().atZone(ZoneOffset.UTC).toInstant();
                    }
                },

                new Converter<Instant, LocalDate>() {
                    @Override
                    public LocalDate convert(Instant source) {
                        return source.atZone(ZoneOffset.UTC).toLocalDate();
                    }
                }
        ));
    }

}
