package com.github.ankurpathak.api.service;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICityService {
    List<String> findStates();

    List<String> findDistricts(String state);

    List<String> findDistricts();

    Page<String> findPinCodes(String state, String district, Pageable pageable);


    Optional<Document> findPinCode(String pinCode);

}
