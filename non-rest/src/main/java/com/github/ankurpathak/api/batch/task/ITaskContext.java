package com.github.ankurpathak.api.batch.task;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;

import java.util.Optional;

public interface ITaskContext {
    Optional<Task> getTask();
    Optional<User> getUser();
    Optional<Business> getBusiness();
}
