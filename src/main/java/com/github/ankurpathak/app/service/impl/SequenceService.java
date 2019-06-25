package com.github.ankurpathak.app.service.impl;

import com.github.ankurpathak.app.domain.repository.mongo.ISequenceRepository;
import com.github.ankurpathak.app.domain.model.Sequence;
import com.github.ankurpathak.app.service.ISequenceService;
import org.springframework.stereotype.Service;

@Service
public class SequenceService extends AbstractDomainService<Sequence, String> implements ISequenceService {

    private final ISequenceRepository dao;

    public SequenceService(ISequenceRepository dao) {
        super(dao);
        this.dao = dao;
    }

    @Override
    public Sequence init() {
        return dao.insert(Sequence.getUserSequenceInitialValue());
    }
}
