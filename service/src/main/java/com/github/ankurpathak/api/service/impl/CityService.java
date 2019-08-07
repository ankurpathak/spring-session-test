package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.ICityRepository;
import com.github.ankurpathak.api.service.ICityService;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService implements ICityService {
    private final ICityRepository dao;

    public CityService(ICityRepository dao) {
        this.dao = dao;
    }

    @Override
    public List<String> findStates() {
        return dao.findStates();
    }

    @Override
    public List<String> findDistricts(String state) {
        return dao.findDistricts(state);
    }

    @Override
    public List<String> findDistricts() {
        return dao.findDistricts();
    }

    @Override
    public Page<String> findPinCodes(String state, String district, Pageable pageable) {
        return dao.findPinCodes(state, district, pageable);
    }

    @Override
    public Optional<Document> findPinCode(String pinCode) {
        return dao.findPinCode(pinCode);
    }
}
