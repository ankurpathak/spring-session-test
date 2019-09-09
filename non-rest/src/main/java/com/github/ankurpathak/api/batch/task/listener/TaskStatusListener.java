package com.github.ankurpathak.api.batch.task.listener;


import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.ITaskService;
import com.github.ankurpathak.api.service.impl.UserService;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.Properties;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

public class TaskStatusListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final ITaskService taskService;
    private final CustomUserDetailsService userDetailsService;
    private final IBusinessService businessService;
    private final IFileService fileService;


    public TaskStatusListener(ITaskService taskService, CustomUserDetailsService userDetailsService, IBusinessService businessService, IFileService fileService) {
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
        this.businessService = businessService;
        this.fileService = fileService;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        Optional<Task> task = taskService.findById(jobExecution.getJobParameters().getString("taskId"));
        ensure(task.isPresent());
        Optional<User> user = userDetailsService.getUserService().findById(PrimitiveUtils.toBigInteger(jobExecution.getJobParameters().getString("userId")));
        ensure(user.isPresent());
        Optional<Business> business = businessService.findById(PrimitiveUtils.toBigInteger(jobExecution.getJobParameters().getString("businessId")));
        ensure(business.isPresent());
        Optional<FileContext> file = fileService.findById(jobExecution.getJobParameters().getString("fileId"));
        ensure(file.isPresent());
        jobExecution.getExecutionContext().put("task", task.get());
        jobExecution.getExecutionContext().put("user", user.get());
        jobExecution.getExecutionContext().put("business", business.get());
        jobExecution.getExecutionContext().put("file", file.get());
        task.get().status(Task.TaskStatus.RUNNING);
        taskService.update(task.get());
    }




    @Override
    public void afterJob(JobExecution jobExecution) {
        Task task= (Task) PrimitiveUtils.cast(jobExecution.getExecutionContext().get("task"), Task.class);
        ensure(task, notNullValue());
        task.status(Task.TaskStatus.COMPLETED);
        taskService.update(task);
    }
}
