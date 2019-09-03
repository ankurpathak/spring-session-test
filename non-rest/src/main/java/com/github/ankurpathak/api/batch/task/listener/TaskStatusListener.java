package com.github.ankurpathak.api.batch.task.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.service.ITaskService;
import com.github.ankurpathak.api.service.impl.UserService;
import com.github.ankurpathak.api.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.io.IOException;

public class TaskStatusListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final ITaskService taskService;
    private final ObjectMapper objectMapper;

    public TaskStatusListener(ITaskService taskService, ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.objectMapper = objectMapper;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        String taskJson = jobExecution.getJobParameters().getString("task");
        if(StringUtils.isNotEmpty(taskJson)){
            try{
                Task task = objectMapper.readValue(taskJson, Task.class);
                task.status(Task.TaskStatus.RUNNING);
                taskService.update(task);
            }catch (IOException ex){
                LogUtil.logStackTrace(log, ex);
            }
        }


    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String taskJson = jobExecution.getJobParameters().getString("task");
        if(StringUtils.isNotEmpty(taskJson)){
            try{
                Task task = objectMapper.readValue(taskJson, Task.class);
                task.status(Task.TaskStatus.COMPLETED);
                taskService.update(task);
            }catch (IOException ex){
                LogUtil.logStackTrace(log, ex);
            }
        }
    }
}
