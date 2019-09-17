package com.github.ankurpathak.api.batch.item.writer.listener;

import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.batch.task.exception.handler.impl.DuplicateKeyExceptionHandler;
import com.github.ankurpathak.api.batch.task.exception.handler.impl.ExceptionHandler;
import com.github.ankurpathak.api.batch.task.exception.handler.impl.FoundExceptionHandler;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.ValidationException;
import com.github.ankurpathak.api.service.impl.util.DuplicateKeyExceptionProcessor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.dao.DuplicateKeyException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class DomainItemWriteListener<T extends Domain<ID>, ID extends Serializable> implements ItemWriteListener<T> {
    private final Class<T> tClass;

    private final DuplicateKeyExceptionHandler duplicateKeyExceptionHandler;
    private final FoundExceptionHandler foundExceptionHandler;
    private final ExceptionHandler exceptionHandler;

    public DomainItemWriteListener(Class<T> tClass, DuplicateKeyExceptionHandler duplicateKeyExceptionHandler, FoundExceptionHandler foundExceptionHandler, ExceptionHandler exceptionHandler) {
        this.tClass = tClass;
        this.duplicateKeyExceptionHandler = duplicateKeyExceptionHandler;
        this.foundExceptionHandler = foundExceptionHandler;
        this.exceptionHandler = exceptionHandler;
    }




    @Override
    public void beforeWrite(List<? extends T> items) {

    }

    @Override
    public void afterWrite(List<? extends T> items) {

    }

    @Override
    public void onWriteError(Exception ex, List<? extends T> items) {
        final Map<String, Object> response;
        if(DuplicateKeyException.class.isInstance(ex)){
            DuplicateKeyException dEx = DuplicateKeyException.class.cast(ex);
            Optional<FoundException> fEx = DuplicateKeyExceptionProcessor.processDuplicateKeyException(dEx, tClass);
            if(fEx.isPresent()){
                response = foundExceptionHandler.handelException(fEx.get());
            }else {
                response = duplicateKeyExceptionHandler.handelException(dEx);
            }
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
