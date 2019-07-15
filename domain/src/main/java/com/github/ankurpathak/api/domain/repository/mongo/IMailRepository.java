package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Mail;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedMailRepository;

public interface IMailRepository extends ExtendedMongoRepository<Mail, String>, CustomizedMailRepository {
}
