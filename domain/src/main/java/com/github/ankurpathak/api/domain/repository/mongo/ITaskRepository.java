package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTaskRepository;

public interface ITaskRepository extends ExtendedMongoRepository<Task, String>, CustomizedTaskRepository {
}
