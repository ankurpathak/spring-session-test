package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.AbstractBatchIntegrationTest;
import com.github.ankurpathak.api.NonRestCmdApplication;
import com.github.ankurpathak.api.config.test.TaskConfig;
import com.github.ankurpathak.api.config.test.MongoConfig;
import com.github.ankurpathak.api.config.test.RedisConfig;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.ITaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {NonRestCmdApplication.class, MongoConfig.class, RedisConfig.class, TaskConfig.class})
@ActiveProfiles("test")
public class ProductCsvTaskTests extends AbstractBatchIntegrationTest<ProductCsvTaskTests> {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private IFileService fileService;

    @Autowired
    private ITaskService taskService;

    @Test
    public void testLaunchCorrectCsv() throws Exception {
        Resource csv = new ClassPathResource("service.csv", this.getClass());
        MockMultipartFile csvMf = new MockMultipartFile("csv", csv.getFilename(), "text/csv", csv.getInputStream());
        String fileId = fileService.store(csvMf);
        Optional<Task> task = taskService.findAll().stream().findFirst();
        task.ifPresent(x -> {
            x.addRequestParam("fileId", fileId);
            x.addRequestParam("taskId", x.getId());
            taskService.update(x);
        });
        JobParameters jps= new JobParametersBuilder()
                .addString("taskId", task.map(Task::getId).get())
                .addString("userId", String.valueOf(2))
                .addString("businessId", String.valueOf(1))
                .addString("fileId", fileId)
                .addString("fileType", Task.TaskType.CSV_PRODUCT)
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
    }

    @Test
    public void testLaunchCorrectCsvEmpty() throws Exception {
        Resource csv = new ClassPathResource("service-empty.csv", this.getClass());
        MockMultipartFile csvMf = new MockMultipartFile("csv", csv.getFilename(), "text/csv", csv.getInputStream());
        String fileId = fileService.store(csvMf);
        Optional<Task> task = taskService.findAll().stream().findFirst();
        task.ifPresent(x -> {
            x.addRequestParam("fileId", fileId);
            x.addRequestParam("taskId", x.getId());
            taskService.update(x);
        });
        JobParameters jps= new JobParametersBuilder()
                .addString("taskId", task.map(Task::getId).get())
                .addString("userId", String.valueOf(2))
                .addString("businessId", String.valueOf(1))
                .addString("fileId", fileId)
                .addString("fileType", Task.TaskType.CSV_PRODUCT)
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
    }

    @Test
    public void testLaunchCorrectCsvMissingHeader() throws Exception {
        Resource csv = new ClassPathResource("service-missing-header.csv", this.getClass());
        MockMultipartFile csvMf = new MockMultipartFile("csv", csv.getFilename(), "text/csv", csv.getInputStream());
        String fileId = fileService.store(csvMf);
        Optional<Task> task = taskService.findAll().stream().findFirst();
        task.ifPresent(x -> {
            x.addRequestParam("fileId", fileId);
            x.addRequestParam("taskId", x.getId());
            taskService.update(x);
        });
        JobParameters jps= new JobParametersBuilder()
                .addString("taskId", task.map(Task::getId).get())
                .addString("userId", String.valueOf(2))
                .addString("businessId", String.valueOf(1))
                .addString("fileId", fileId)
                .addString("fileType", Task.TaskType.CSV_PRODUCT)
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
    }
}
