package com.github.ankurpathak.api.domain.repository.rabbitmq.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.repository.IMessageSenderRepository;
import com.github.ankurpathak.api.domain.repository.dto.MessageContext;
import com.github.ankurpathak.api.exception.ServiceException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class RabbitMessageSenderRepository implements IMessageSenderRepository {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitMessageSenderRepository(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }


    @Override
    public void send(MessageContext context){
        try{
            Message message = MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(context.getMessage()))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            rabbitTemplate.convertAndSend(context.getTarget(), context.getKey(), message);
        }catch (IOException ex){
            throw new ServiceException(ex.getMessage(), ex.getCause());
        }

    }
}
