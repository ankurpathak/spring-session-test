package com.ankurpathak.springsessiontest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

@SpringBootApplication
public class SpringSessionTestApplication {


    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "com.ankurpathak.springsessiontest.ExtendedInheritableThreadLocalSecurityContextHolderStrategy");
        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}


@Component
class ApplicationRunnerImpl implements ApplicationRunner{

    @Autowired
    private LocalValidatorFactoryBean validator;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        UserDto dto = UserDto.getInstance().firstName("").lastName("hello");
        BindException result = new BindException(dto, dto.getClass().getSimpleName());
        validator.validate(dto, result, UserDto.Default.class);

       // Set<ConstraintViolation<UserDto>> result = validator.validate();
       // System.out.println(result.size());

        System.out.println(result.hasErrors());
    }
}
