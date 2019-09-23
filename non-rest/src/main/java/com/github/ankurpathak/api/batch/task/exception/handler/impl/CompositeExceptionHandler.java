package com.github.ankurpathak.api.batch.task.exception.handler.impl;

import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CompositeExceptionHandler implements IExceptionHandler<Exception> {

    private final List<IExceptionHandler<?>> exceptionHandlers;

    public CompositeExceptionHandler(List<IExceptionHandler<?>> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    @Override
    public Map<String, Object> handelException(Exception ex) {
        for(IExceptionHandler<?> exceptionHandler: exceptionHandlers){
            if(exceptionHandler.supports(ex)){
               return exceptionHandler.handelException(ex);
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean supports(Exception ex) {
        return false;
    }
}
