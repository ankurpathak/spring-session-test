package com.ankurpathak.springsessiontest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@EnableMongoAuditing(
        auditorAwareRef = "userIdAuditorAware",
        dateTimeProviderRef = "instantDateTimeProvider"
)
@EnableCaching
public class SpringSessionTestApplication {


    public static void main(String[] args) {

        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}


@Component
class ApplicationRunnerImpl implements ApplicationRunner{


    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
