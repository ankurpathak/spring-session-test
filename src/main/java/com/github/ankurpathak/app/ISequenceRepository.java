package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.repository.custom.CustomizedSequenceRepository;

public interface ISequenceRepository extends ExtendedMongoRepository<Sequence, String>, CustomizedSequenceRepository {
}
