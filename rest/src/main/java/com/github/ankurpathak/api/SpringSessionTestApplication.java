package com.github.ankurpathak.api;

import com.github.ankurpathak.api.domain.repository.IFileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@SpringBootApplication
public class SpringSessionTestApplication {


    public static void main(String[] args) {
        System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");
        SpringApplication.run(SpringSessionTestApplication.class, args);
        System.out.println();
    }
}

//@Component
class TestCmd implements CommandLineRunner {
    private final IFileRepository fileRepository;

    TestCmd(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        ClassPathResource resource = new ClassPathResource("application.properties");
        fileRepository.store(resource.getInputStream(), resource.getFilename(), "text/plain", Collections.emptyMap());
    }
}
