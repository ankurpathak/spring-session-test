package com.github.ankurpathak.api.domain.repository.mongo;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICityRepository {

    List<String> findStates();

    @SuppressWarnings("unchecked")
    List<String> findDistricts(String state);

    @SuppressWarnings("unchecked")
    List<String> findDistricts();

    Page<String> findPinCodes(String state, String district, Pageable pageable);

    Optional<Document> findPinCode(String pinCode);
}
