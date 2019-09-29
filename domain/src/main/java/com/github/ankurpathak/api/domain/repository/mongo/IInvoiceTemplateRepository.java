package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Invoice;
import com.github.ankurpathak.api.domain.model.InvoiceTemplate;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedInvoiceRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedInvoiceTemplateRepository;

public interface IInvoiceTemplateRepository extends ExtendedMongoRepository<InvoiceTemplate, String>, CustomizedInvoiceTemplateRepository {
}
