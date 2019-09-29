package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Invoice;
import com.github.ankurpathak.api.domain.model.Notification;
import com.github.ankurpathak.api.domain.repository.mongo.ExtendedMongoRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedInvoiceRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedNotificationRepository;

public interface IInvoiceRepository extends ExtendedMongoRepository<Invoice, String>, CustomizedInvoiceRepository {
}
