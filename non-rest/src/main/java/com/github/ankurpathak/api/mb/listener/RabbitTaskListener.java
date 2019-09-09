package com.github.ankurpathak.api.mb.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.config.RabbitConfig;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.mb.listener.impl.ITaskListener;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class RabbitTaskListener implements ITaskListener {
    private static final Logger log = LoggerFactory.getLogger(RabbitTaskListener.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Map<String, Job> jobs;

    @Override
    @RabbitListener(queues = RabbitConfig.TASK_QUEUE)
    public void process(Map<String, String> request)  {
        String taskType = request.get("taskType");
        if(Objects.nonNull(taskType)) {
            JobParametersBuilder jpb = new JobParametersBuilder();
            request.keySet().stream().map(key -> jpb.addString(key, request.get(key)));
            switch (taskType) {
                case Task.TaskType.CSV_PRODUCT:
                    try {
                        Job job = jobs.get(taskType);
                        jobLauncher.run(job, jpb.toJobParameters());
                    } catch (Exception ex) {
                        LogUtil.logStackTrace(log, ex);
                    }
                    break;
            }
        }
    }
}
