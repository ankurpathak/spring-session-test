package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.ICountryEtlRepository;
import com.github.ankurpathak.api.service.ICountryEtlService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CountryEtlService implements ICountryEtlService {
    private final ICountryEtlRepository countryEtlRepository;

    public CountryEtlService(ICountryEtlRepository countryEtlRepository) {
        this.countryEtlRepository = countryEtlRepository;
    }


    @Override
    public void process() throws IOException {
        countryEtlRepository.process();
    }
}
