package com.github.ankurpathak.api.batch.task.listener;


import com.github.ankurpathak.api.batch.task.CsvTaskContext;
import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IFileService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.ITaskService;
import com.github.ankurpathak.api.util.PrimitiveUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.transaction.annotation.Transactional;

public class TaskStatusListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(TaskStatusListener.class);

    private final ITaskService taskService;
    private final CustomUserDetailsService userDetailsService;
    private final IBusinessService businessService;
    private final IFileService fileService;
    private final IMessageService messageService;


    public TaskStatusListener(ITaskService taskService, CustomUserDetailsService userDetailsService, IBusinessService businessService, IFileService fileService, IMessageService messageService) {
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
        this.businessService = businessService;
        this.fileService = fileService;
        this.messageService = messageService;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        var task = this.taskService.findById(jobExecution.getJobParameters().getString("taskId"));
        CsvTaskContext csvTaskContext = new CsvTaskContext()
                .task(task.orElse(null))
                .user(this.userDetailsService.getUserService().findById(PrimitiveUtils.toBigInteger(jobExecution.getJobParameters().getString("userId"))).orElse(null))
                .business(this.businessService.findById(PrimitiveUtils.toBigInteger(jobExecution.getJobParameters().getString("businessId"))).orElse(null))
                .file(this.fileService.findById(jobExecution.getJobParameters().getString("fileId")).orElse(null));
        csvTaskContext.afterPropertiesSet();
        TaskContextHolder.setContext(csvTaskContext);
        task.map(x -> x.status(Task.TaskStatus.RUNNING))
                .ifPresent(this.taskService::update);
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        TaskContextHolder
                .getContext()
                .flatMap(ITaskContext::getTask)
                .map(task -> {
                            if (task.getStatus() == Task.TaskStatus.RUNNING) {
                                return task.status(Task.TaskStatus.COMPLETED)
                                        .addRequestParam("taskId", task.getId())
                                        .response(ApiResponse.getInstance(ApiCode.SUCCESS, this.messageService.getMessage(ApiMessages.SUCCESS)).getExtras());
                            }
                            return task;
                        }

                )
                .ifPresent(this.taskService::update);
        TaskContextHolder.clearContext();
    }
}
