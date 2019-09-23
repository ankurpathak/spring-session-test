package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;

import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public interface ITaskContext {
    Task getTask();
    User getUser();
    Business getBusiness();
    String getRequestedBusinessId();

    default void afterPropertiesSet(){
        require(this.getTask(), notNullValue());
        require(this.getUser(), notNullValue());
        require(this.getBusiness(), notNullValue());
        require(this.getRequestedBusinessId(), notNullValue());
    }
}
