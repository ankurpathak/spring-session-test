package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.AbstractBatchIntegrationTest;
import com.github.ankurpathak.api.NonRestCmdApplication;
import com.github.ankurpathak.api.batch.test.ExtendedJobLauncherTestUtils;
import com.github.ankurpathak.api.config.test.MongoConfig;
import com.github.ankurpathak.api.config.test.RedisConfig;
import com.github.ankurpathak.api.config.test.TaskConfig;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controllor.dto.CustomerDto;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.ITaskService;
import com.github.ankurpathak.api.util.LogUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.doesNotHave;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {NonRestCmdApplication.class, MongoConfig.class, RedisConfig.class, TaskConfig.class})
@ActiveProfiles("test")
public class CustomerCsvTaskTests extends AbstractBatchIntegrationTest<CustomerCsvTaskTests> {


    private static final Logger log = LoggerFactory.getLogger(CustomerCsvTaskTests.class);


    @Autowired
    private IFileService fileService;

    @Autowired
    private ITaskService taskService;


    @PostConstruct
    public void setupJob(){
        Job job = this.applicationContext.getBean(Task.TaskType.CSV_CUSTOMER, Job.class);
        this.jobLauncherTestUtils = new ExtendedJobLauncherTestUtils();
        this.applicationContext.getAutowireCapableBeanFactory().autowireBean(this.jobLauncherTestUtils);
        this.applicationContext.registerBean(JobLauncherTestUtils.class, () -> this.jobLauncherTestUtils);
        this.jobLauncherTestUtils.setJob(job);
    }

    @Test
    public void testLaunchCorrectCsv() throws Exception {
        Resource csv = new ClassPathResource("customer.csv", this.getClass());
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
                .addString("taskType", Task.TaskType.CSV_CUSTOMER)
                .addString("requestedBusinessId", String.valueOf(1))
                .toJobParameters();
        jobLauncherTestUtils.launchJob(jps);
        Task taskResult = taskService.findAll().stream().findFirst().orElse(null);
        assertThat(taskResult).isNotNull();
        assertThat(taskResult.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(taskResult.getResponse()).isNotNull()
                .isNotEmpty()
                .containsAllEntriesOf(Map.of("code", ApiCode.SUCCESS.getCode()))
                .containsKey("message")
                .containsValue("Success.");
        LogUtil.logValue(log, "task", objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(taskResult));
    }

    @Test
    public void testLaunchCsvEmpty() throws Exception {
        Resource csv = new ClassPathResource("customer-empty.csv", this.getClass());
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
                .addString("taskType", Task.TaskType.CSV_CUSTOMER)
                .addString("requestedBusinessId", String.valueOf(1))
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
        Task taskResult = taskService.findAll().stream().findFirst().orElse(null);

        assertThat(taskResult).isNotNull();
        assertThat(taskResult.getStatus()).isEqualTo(Task.TaskStatus.ERROR);
        assertThat(taskResult.getResponse()).isNotNull()
                .isNotEmpty()
                .containsAllEntriesOf(Map.of("code", ApiCode.INVALID_CSV.getCode()))
                .containsKey("message");
        LogUtil.logValue(log, "task", objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(taskResult));

    }

    @Test
    public void testLaunchMissingHeader() throws Exception {
        Resource csv = new ClassPathResource("customer-missing-header.csv", this.getClass());
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
                .addString("taskType", Task.TaskType.CSV_CUSTOMER)
                .addString("requestedBusinessId", String.valueOf(1))
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
        Task taskResult = taskService.findAll().stream().findFirst().orElse(null);
        assertThat(taskResult.getStatus()).isEqualTo(Task.TaskStatus.ERROR);
        assertThat(taskResult).isNotNull();
        assertThat(taskResult.getResponse()).isNotNull()
                .isNotEmpty()
                .containsAllEntriesOf(Map.of("code", ApiCode.INVALID_CSV.getCode()))
                .containsKey("message");
        LogUtil.logValue(log, "task", objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(taskResult));

    }

    @Test
    public void testLaunchHeaderFieldMismatch() throws Exception {
        Resource csv = new ClassPathResource("customer-header-field-mismatch.csv", this.getClass());
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
                .addString("taskType", Task.TaskType.CSV_CUSTOMER)
                .addString("requestedBusinessId", String.valueOf(1))
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
        Task taskResult = taskService.findAll().stream().findFirst().orElse(null);
        assertThat(taskResult.getStatus()).isEqualTo(Task.TaskStatus.ERROR);
        assertThat(taskResult).isNotNull();
        assertThat(taskResult.getResponse()).isNotNull()
                .isNotEmpty()
                .containsAllEntriesOf(Map.of("code", ApiCode.INVALID_CSV.getCode()))
                .containsKey("message");
        LogUtil.logValue(log, "task", objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(taskResult));

    }

    @Test
    public void testLaunchMissingRequiredField() throws Exception {
        Resource csv = new ClassPathResource("customer-missing-required-field.csv", this.getClass());
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
                .addString("taskType", Task.TaskType.CSV_CUSTOMER)
                .addString("requestedBusinessId", String.valueOf(1))
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
        Task taskResult = taskService.findAll().stream().findFirst().orElse(null);
        assertThat(taskResult.getStatus()).isEqualTo(Task.TaskStatus.ERROR);
        assertThat(taskResult).isNotNull();
        assertThat(taskResult.getResponse()).isNotNull()
                .isNotEmpty()
                .containsAllEntriesOf(Map.of("code", ApiCode.INVALID_CSV.getCode()))
                .containsKey("message");
        LogUtil.logValue(log, "task", objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(taskResult));

    }


    @Test
    public void testLaunchMissingValidation() throws Exception {
        Resource csv = new ClassPathResource("customer-missing-validation.csv", this.getClass());
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
                .addString("taskType", Task.TaskType.CSV_CUSTOMER)
                .addString("requestedBusinessId", String.valueOf(1))
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
        Task taskResult = taskService.findAll().stream().findFirst().orElse(null);
        assertThat(taskResult.getStatus()).isEqualTo(Task.TaskStatus.ERROR);
        assertThat(taskResult).isNotNull();
        assertThat(taskResult.getResponse()).isNotNull()
                .isNotEmpty()
                .containsAllEntriesOf(Map.of("code", ApiCode.VALIDATION.getCode()))
                .containsKey("message");
        LogUtil.logValue(log, "task", objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(taskResult));
    }


    @Test
    @Ignore
    public void testLaunchWithDuplicates() throws Exception {
        Resource csv = new ClassPathResource("service-with-duplicate.csv", this.getClass());
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
                .addString("taskType", Task.TaskType.CSV_CUSTOMER)
                .addString("requestedBusinessId", String.valueOf(1))
                .toJobParameters();
        Job job = jobLauncherTestUtils.getJob();
        jobLauncherTestUtils.launchJob(jps);
        Task taskResult = taskService.findAll().stream().findFirst().orElse(null);
        assertThat(taskResult.getStatus()).isEqualTo(Task.TaskStatus.ERROR);
        assertThat(taskResult).isNotNull();
        assertThat(taskResult.getResponse()).isNotNull()
                .isNotEmpty()
                .containsAllEntriesOf(Map.of("code", ApiCode.FOUND.getCode()))
                .containsKey("message");
        LogUtil.logValue(log, "task", objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(taskResult));
    }

}
