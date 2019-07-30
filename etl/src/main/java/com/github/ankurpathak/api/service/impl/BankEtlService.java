package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.IBankEltRepository;
import com.github.ankurpathak.api.service.IBankEtlService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BankEtlService implements IBankEtlService {
    private final IBankEltRepository bankIfscEltRepository;

    public BankEtlService(IBankEltRepository bankIfscEltRepository) {
        this.bankIfscEltRepository = bankIfscEltRepository;
    }

    @Override
    public void process() throws IOException {
        bankIfscEltRepository.process();
    }
}
