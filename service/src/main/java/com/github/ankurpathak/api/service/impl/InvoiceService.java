package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Invoice;
import com.github.ankurpathak.api.domain.repository.mongo.IInvoiceRepository;
import com.github.ankurpathak.api.service.IInvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InvoiceService extends AbstractDomainService<Invoice, String> implements IInvoiceService {

    private final IInvoiceRepository dao;

    public InvoiceService(IInvoiceRepository dao) {
        super(dao);
        this.dao = dao;
    }


}
