package com.github.ankurpathak.api;

import com.github.ankurpathak.api.service.IBankEtlService;
import com.github.ankurpathak.api.service.ICityEtlService;
import com.github.ankurpathak.api.service.ISchemaService;
import com.github.ankurpathak.api.util.LogUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSessionTestApplication {


    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");
        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}
