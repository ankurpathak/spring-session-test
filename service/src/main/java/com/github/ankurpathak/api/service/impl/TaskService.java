package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.repository.mongo.ITaskRepository;
import com.github.ankurpathak.api.service.ITaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskService extends AbstractDomainService<Task, String> implements ITaskService {

    private final ITaskRepository dao;

    public TaskService(ITaskRepository dao) {
        super(dao);
        this.dao = dao;
    }
}
