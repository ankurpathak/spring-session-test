package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.IBankIfscEltRepository;
import com.github.ankurpathak.api.domain.repository.mongo.ICityEtlRepository;
import com.github.ankurpathak.api.service.IBankIfscEtlService;
import com.github.ankurpathak.api.service.ICityEtlService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CityEtlService implements ICityEtlService {
    private final ICityEtlRepository cityEtlRepository;


    public CityEtlService(ICityEtlRepository cityEtlRepository) {
        this.cityEtlRepository = cityEtlRepository;
    }

    @Override
    public void process() throws IOException {
        cityEtlRepository.process();
    }
}
