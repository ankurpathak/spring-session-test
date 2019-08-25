package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.ITextRepository;
import com.github.ankurpathak.api.service.ITextService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskService extends AbstractDomainService<Text, String> implements ITextService {

    private final ITextRepository dao;

    public TaskService(ITextRepository dao) {
        super(dao);
        this.dao = dao;
    }
}
