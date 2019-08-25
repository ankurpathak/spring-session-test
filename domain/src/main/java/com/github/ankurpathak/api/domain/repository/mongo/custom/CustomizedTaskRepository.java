package com.github.ankurpathak.api.domain.repository.mongo.custom;

import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomizedDomainRepository;

public interface CustomizedTaskRepository extends ICustomizedDomainRepository<Task, String> {
}
