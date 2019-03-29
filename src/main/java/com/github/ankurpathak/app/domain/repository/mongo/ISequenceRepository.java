package com.github.ankurpathak.app.domain.repository.mongo;

import com.github.ankurpathak.app.Sequence;
import com.github.ankurpathak.app.domain.repository.mongo.custom.CustomizedSequenceRepository;

public interface ISequenceRepository extends ExtendedMongoRepository<Sequence, String>, CustomizedSequenceRepository {
}
