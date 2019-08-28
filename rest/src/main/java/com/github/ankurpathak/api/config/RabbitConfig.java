package com.github.ankurpathak.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig  {
    public static final String TASK_QUEUE = "task-queue";
    public static final String DEAD_TASK_QUEUE = "dead-task-queue";
    public static final String TASK_EXCHANGE = "task-exchange";


    @Bean(name = TASK_QUEUE)
    public Queue taskQueue() {
        return QueueBuilder.durable(TASK_QUEUE).build();
    }

    @Bean(name = DEAD_TASK_QUEUE)
    public Queue deadTaskQueue() {
        return QueueBuilder.durable(DEAD_TASK_QUEUE).build();
    }

    @Bean(name = TASK_EXCHANGE)
    public Exchange taskExchange() {
        return ExchangeBuilder.directExchange(TASK_EXCHANGE).build();
    }

    @Bean
    public Binding binding(@Qualifier(TASK_QUEUE) Queue taskQueue, @Qualifier(TASK_EXCHANGE) Exchange taskExchange) {
        return BindingBuilder.bind(taskQueue).to(taskExchange).with(TASK_QUEUE).noargs();
    }
}