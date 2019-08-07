package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.IBankRepository;
import com.github.ankurpathak.api.service.IBankService;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService implements IBankService {
    private final IBankRepository bankRepository;

    public BankService(IBankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }


    @Override
    public List<Document> findNames() {
        return bankRepository.findNames();
    }

    @Override
    public List<String> findStates(String code) {
        return bankRepository.findStates(code);
    }

    @Override
    public List<String> findDistrict(String code, String state) {
        return bankRepository.findDistrict(code, state);
    }

    @Override
    public List<Document> findBranches(String code, String state, String district) {
        return bankRepository.findBranches(code, state, district);
    }

    @Override
    public Optional<Document> findBranch(String ifsc) {
        return bankRepository.findBranch(ifsc);
    }


}
