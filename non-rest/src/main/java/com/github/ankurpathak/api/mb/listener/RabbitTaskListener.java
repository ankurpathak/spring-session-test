package com.github.ankurpathak.api.mb.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.config.RabbitConfig;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.mb.listener.impl.ITaskListener;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RabbitTaskListener implements ITaskListener {
    private static final Logger log = LoggerFactory.getLogger(RabbitTaskListener.class);


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Map<String, Job> jobs;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @RabbitListener(queues = RabbitConfig.TASK_QUEUE)
    public void process(Task task)  {
        switch (task.getType()){
            case CSV_PRODUCT:

                try {
                    Job job  = jobs.get("productCsvTask");
                    jobLauncher.run(job, new JobParametersBuilder().addString("task", objectMapper.writeValueAsString(task)).toJobParameters());
                } catch (Exception ex){
                    LogUtil.logStackTrace(log,  ex);
                }

                break;
        }
    }
}
