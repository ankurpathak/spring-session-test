package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Notification;
import com.github.ankurpathak.api.domain.model.Text;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedNotificationRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTextRepository;

public interface INotificationRepository extends ExtendedMongoRepository<Notification, String>, CustomizedNotificationRepository {
}
