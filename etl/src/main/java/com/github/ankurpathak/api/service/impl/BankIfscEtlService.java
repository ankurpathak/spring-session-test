package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.IBankIfscEltRepository;
import com.github.ankurpathak.api.service.IBankIfscEtlService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BankIfscEtlService implements IBankIfscEtlService {
    private final IBankIfscEltRepository bankIfscEltRepository;

    public BankIfscEtlService(IBankIfscEltRepository bankIfscEltRepository) {
        this.bankIfscEltRepository = bankIfscEltRepository;
    }

    @Override
    public void process() throws IOException {
        bankIfscEltRepository.process();
    }
}
