package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.Sequence;

public interface ISequenceService extends IDomainService<Sequence, String> {
    Sequence init();
}
