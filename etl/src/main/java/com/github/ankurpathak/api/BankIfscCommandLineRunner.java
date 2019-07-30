package com.github.ankurpathak.api;

import com.github.ankurpathak.api.service.IBankEtlService;
import org.springframework.boot.CommandLineRunner;

public class BankIfscCommandLineRunner implements CommandLineRunner {

    private final IBankEtlService bankIfscEtlService;

    public BankIfscCommandLineRunner(IBankEtlService bankIfscEtlService) {
        this.bankIfscEtlService = bankIfscEtlService;
    }

    @Override
    public void run(String... args) throws Exception {
        bankIfscEtlService.process();
    }
}
