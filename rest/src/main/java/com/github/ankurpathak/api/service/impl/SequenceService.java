package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.ISequenceRepository;
import com.github.ankurpathak.api.domain.model.Sequence;
import com.github.ankurpathak.api.service.ISequenceService;
import org.springframework.stereotype.Service;

@Service
public class SequenceService extends AbstractDomainService<Sequence, String> implements ISequenceService {

    private final ISequenceRepository dao;

    public SequenceService(ISequenceRepository dao) {
        super(dao);
        this.dao = dao;
    }

    @Override
    public void init() {
        dao.insert(Sequence.getUserSequenceInitialValue());
        dao.insert(Sequence.getBusinessSequenceInitialValue());
    }
}
