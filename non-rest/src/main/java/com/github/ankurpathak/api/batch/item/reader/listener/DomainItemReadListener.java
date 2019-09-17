package com.github.ankurpathak.api.batch.item.reader.listener;

import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.service.ITaskService;
import org.springframework.batch.core.ItemReadListener;

import java.io.Serializable;
import java.util.Map;


public class DomainItemReadListener<Tdto extends DomainDto<T, ID> , ID extends Serializable, T extends Domain<ID>> implements ItemReadListener<Tdto> {
    private final Class<Tdto> tDtoClass;
    private final ITaskService taskService;
    private final IExceptionHandler<?> exceptionHandler;
    public DomainItemReadListener(Class<Tdto> tDtoClass, ITaskService taskService, IExceptionHandler<?> exceptionHandler) {
        this.tDtoClass = tDtoClass;
        this.taskService = taskService;
        this.exceptionHandler = exceptionHandler;
    }



    @Override
    public void beforeRead() {

    }
    @Override
    public void afterRead(Tdto item) {

    }
    @Override
    public void onReadError(Exception ex) {
        final Map<String, Object> response =  exceptionHandler.handelException(ex);
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
