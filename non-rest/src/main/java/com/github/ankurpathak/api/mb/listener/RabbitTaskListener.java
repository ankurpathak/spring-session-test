package com.github.ankurpathak.api.mb.listener;

import com.github.ankurpathak.api.config.RabbitConfig;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.mb.listener.impl.ITaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitTaskListener implements ITaskListener {
    private static final Logger log = LoggerFactory.getLogger(RabbitTaskListener.class);

    @Override
    @RabbitListener(queues = RabbitConfig.TASK_QUEUE)
    public void process(Task task) {
        switch (task.getType()){
            case CSV_PRODUCT:
                break;
        }
    }
}
