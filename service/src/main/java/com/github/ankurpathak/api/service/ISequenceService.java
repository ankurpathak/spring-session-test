package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Sequence;

public interface ISequenceService extends IDomainService<Sequence, String> {
    void init();
}
