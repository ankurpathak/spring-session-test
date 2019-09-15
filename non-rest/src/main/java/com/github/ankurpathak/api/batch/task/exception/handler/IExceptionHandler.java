package com.github.ankurpathak.api.batch.task.exception.handler;

import com.github.ankurpathak.api.batch.task.ITaskContext;

import java.util.Map;

public interface IExceptionHandler<T extends Exception> {
    Map<String, Object> handelException(T ex);
}
