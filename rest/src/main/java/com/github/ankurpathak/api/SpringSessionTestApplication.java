package com.github.ankurpathak.api;

import com.github.ankurpathak.api.domain.repository.mongo.ICityEtlRepository;
import com.github.ankurpathak.api.service.IBankService;
import com.github.ankurpathak.api.service.ICityEtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringSessionTestApplication {


    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");
        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}


@Component
class ApplicationRunnerImpl implements ApplicationRunner{

    @Autowired
    private ICityEtlService cityEtlService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        cityEtlService.process();
    }
}
