package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTextRepository;

public interface ITextRepository extends ExtendedMongoRepository<Text, String>, CustomizedTextRepository {
}
