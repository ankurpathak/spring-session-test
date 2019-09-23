package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.batch.task.exception.handler.impl.*;
import com.github.ankurpathak.api.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ExceptionHandlerConfig {

    @Autowired
    private IMessageService messageService;

    @Bean
    public IExceptionHandler<?> compositeExceptionHandler(){
        return new CompositeExceptionHandler(
                List.of(
                        csvExceptionHandler(),
                        foundExceptionHandler(),
                        duplicateKeyExceptionHandler(),
                        validationExceptionHandler(),
                        notFoundExceptionHandler(),
                        exceptionHandler()
                        )
        );
    }

    protected NotFoundExceptionHandler notFoundExceptionHandler() {
        return new NotFoundExceptionHandler(messageService);
    }


    protected ExceptionHandler exceptionHandler(){
        return new ExceptionHandler(messageService);
    }


    protected CsvExceptionHandler csvExceptionHandler(){
        return new CsvExceptionHandler(messageService);
    }

    protected DuplicateKeyExceptionHandler duplicateKeyExceptionHandler(){
        return new DuplicateKeyExceptionHandler(messageService);
    }


    protected FoundExceptionHandler foundExceptionHandler(){
        return new FoundExceptionHandler(messageService);
    }


    protected ValidationExceptionHandler validationExceptionHandler(){
        return new ValidationExceptionHandler(messageService);
    }
}
