package com.ankurpathak.springsessiontest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringSessionTestApplication {


    public static void main(String[] args) {

        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}


@Component
class ApplicationRunnerImpl implements ApplicationRunner{

    @Value("${server.error.path1:${error.path:/error}}")
    private String property;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(property);
    }
}
