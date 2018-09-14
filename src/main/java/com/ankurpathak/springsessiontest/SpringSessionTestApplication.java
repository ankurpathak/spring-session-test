package com.ankurpathak.springsessiontest;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LinkedinService linkedinService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        var url = linkedinService.authorizationUrl();
        System.out.println(url);
    }
}
