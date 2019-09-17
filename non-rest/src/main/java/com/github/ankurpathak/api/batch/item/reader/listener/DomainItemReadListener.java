package com.github.ankurpathak.api.batch.item.reader.listener;

import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.batch.task.exception.handler.impl.CsvExceptionHandler;
import com.github.ankurpathak.api.batch.task.exception.handler.impl.ExceptionHandler;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.service.ITaskService;
import com.opencsv.exceptions.CsvException;
import org.springframework.batch.core.ItemReadListener;

import java.io.Serializable;
import java.util.Map;


public class DomainItemReadListener<Tdto extends DomainDto<T, ID> , ID extends Serializable, T extends Domain<ID>> implements ItemReadListener<Tdto> {

    private final Class<Tdto> tDtoClass;
    private final ITaskService taskService;
    public DomainItemReadListener(Class<Tdto> tDtoClass, ITaskService taskService, CsvExceptionHandler csvExceptionHandler, ExceptionHandler exceptionHandler) {
        this.tDtoClass = tDtoClass;
        this.taskService = taskService;
        this.csvExceptionHandler = csvExceptionHandler;
        this.exceptionHandler = exceptionHandler;
    }
    private final CsvExceptionHandler csvExceptionHandler;
    private final ExceptionHandler exceptionHandler;


    @Override
    public void beforeRead() {

    }
    @Override
    public void afterRead(Tdto item) {

    }
    @Override
    public void onReadError(Exception ex) {
        final Map<String, Object> response;
        if(RuntimeException.class.isInstance(ex) && CsvException.class.isInstance(ex.getCause())){
            response = csvExceptionHandler.handelException(RuntimeException.class.cast(ex));
        }else {
            response = exceptionHandler.handelException(ex);
        }
        TaskContextHolder
                .getContext()
                .flatMap(ITaskContext::getTask)
                .filter(x -> Task.TaskStatus.RUNNING == x.getStatus())
                .map(x ->
                        x.status(Task.TaskStatus.ERROR)
                                .addRequestParam("taskId", x.getId())
                                .response(response)
                );
    }
}
