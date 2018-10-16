package com.ankurpathak.springsessiontest;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableMongoAuditing(
        auditorAwareRef = "userIdAuditorAware",
        dateTimeProviderRef = "instantDateTimeProvider"
)
@EnableCaching
public class SpringSessionTestApplication {


    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "com.ankurpathak.springsessiontest.ExtendedInheritableThreadLocalSecurityContextHolderStrategy");
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
