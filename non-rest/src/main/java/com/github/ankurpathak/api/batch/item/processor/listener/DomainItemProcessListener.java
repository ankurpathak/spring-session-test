package com.github.ankurpathak.api.batch.item.processor.listener;

import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import org.springframework.batch.core.ItemProcessListener;

import java.io.Serializable;
import java.util.Map;


public class DomainItemProcessListener<Tdto extends DomainDto<T, ID>, ID extends Serializable, T extends Domain<ID>> implements ItemProcessListener<Tdto,  T> {
    private final Class<T> tClass;
    private final Class<Tdto> tDtoClass;
    private final IExceptionHandler<?> exceptionHandler;

    public DomainItemProcessListener(Class<T> tClass, Class<Tdto> tDtoClass, IExceptionHandler<?> exceptionHandler) {
        this.tClass = tClass;
        this.tDtoClass = tDtoClass;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void beforeProcess(Tdto item) {

    }

    @Override
    public void afterProcess(Tdto item, T result) {

    }

    @Override
    public void onProcessError(Tdto item, Exception ex) {
        final Map<String, Object> response = exceptionHandler.handelException(ex);
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
