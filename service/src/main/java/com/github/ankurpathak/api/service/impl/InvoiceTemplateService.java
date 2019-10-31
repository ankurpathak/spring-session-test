package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.InvoiceTemplate;
import com.github.ankurpathak.api.domain.repository.mongo.IInvoiceTemplateRepository;
import com.github.ankurpathak.api.service.IInvoiceTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InvoiceTemplateService extends AbstractDomainService<InvoiceTemplate, String> implements IInvoiceTemplateService {
    private final IInvoiceTemplateRepository dao;
    public InvoiceTemplateService(IInvoiceTemplateRepository dao) {
        super(dao);
        this.dao = dao;
    }
}
