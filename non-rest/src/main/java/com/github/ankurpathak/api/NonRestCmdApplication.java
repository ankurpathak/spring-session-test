package com.github.ankurpathak.api;

import com.github.ankurpathak.api.service.impl.SchedulerService;
import org.quartz.JobDataMap;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@SpringBootApplication
public class NonRestCmdApplication {

    public static void main(String[] args) {
        SpringApplication.run(NonRestCmdApplication.class, args);
    }



}
@Component
class TestClr implements CommandLineRunner{

    private final SchedulerService schedulerService;
    private final JobBuilderFactory jobBuilderFactory;

    TestClr(SchedulerService schedulerService, JobBuilderFactory jobBuilderFactory) {
        this.schedulerService = schedulerService;
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Override
    public void run(String... args) throws Exception {
       schedulerService.scheduleJob(CmdJob.class, CmdJob.class.getSimpleName(), CmdJob.class.getSimpleName(), new JobDataMap(), "0 0/1 * 1/1 * ? *", Instant.now().plusSeconds(30), Instant.now().plus(Duration.ofMinutes(30)));
    }
}

