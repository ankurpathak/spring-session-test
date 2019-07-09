package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Sequence;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedSequenceRepository;

public interface ISequenceRepository extends ExtendedMongoRepository<Sequence, String>, CustomizedSequenceRepository {
}
