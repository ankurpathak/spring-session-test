package com.github.ankurpathak.api.batch.item.writer.listener;

import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.service.impl.util.DuplicateKeyExceptionProcessor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.dao.DuplicateKeyException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class DomainItemWriteListener<T extends Domain<ID>, ID extends Serializable, Tdto extends DomainDto<T,ID>> implements ItemWriteListener<T> {
    private final Class<T> tClass;
    private final Class<Tdto> tDtoClass;
    private IExceptionHandler<?> exceptionHandler;

    public DomainItemWriteListener(Class<T> tClass, Class<Tdto> tDtoClass, IExceptionHandler<?> exceptionHandler) {
        this.tClass = tClass;
        this.tDtoClass =tDtoClass;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void beforeWrite(List<? extends T> items) {

    }

    @Override
    public void afterWrite(List<? extends T> items) {

    }

    @Override
    public void onWriteError(Exception exIn, List<? extends T> items) {
        final Map<String, Object> response;
        Exception exOut = exIn;
        if(DuplicateKeyException.class.isInstance(exIn)){
            Optional<FoundException> fEx = DuplicateKeyExceptionProcessor.processDuplicateKeyException(DuplicateKeyException.class.cast(exIn), tClass);
            if(fEx.isPresent())
                exOut = fEx.get();
        }
        response = exceptionHandler.handelException(exOut);
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
