package com.github.ankurpathak.api;

import com.github.ankurpathak.api.service.IBankIfscEtlService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

public class BankIfscCommandLineRunner implements CommandLineRunner {

    private final IBankIfscEtlService bankIfscEtlService;

    public BankIfscCommandLineRunner(IBankIfscEtlService bankIfscEtlService) {
        this.bankIfscEtlService = bankIfscEtlService;
    }

    @Override
    public void run(String... args) throws Exception {
        bankIfscEtlService.process();
    }
}
