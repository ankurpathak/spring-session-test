package com.github.ankurpathak.api;

import com.github.ankurpathak.api.domain.repository.IFileRepository;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
public class SpringSessionTestApplication {


    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");
        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}

@Component
class TestCmd implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
    }
}
