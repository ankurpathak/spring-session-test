package com.github.ankurpathak.api;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableBatchProcessing
public class NonRestCmdApplication {
    public static void main(String[] args) {
        SpringApplication.run(NonRestCmdApplication.class, args);
    }
}
@Component
class TestClr implements CommandLineRunner{

    @Override
    public void run(String... args) throws Exception {
    }
}

