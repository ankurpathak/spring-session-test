package com.github.ankurpathak.api.domain.repository.mongo;

import org.bson.Document;

import java.util.List;
import java.util.Optional;

public interface IBankRepository {
    List<Document> findNames();

    List<String> findStates(String code);

    List<String> findDistrict(String code, String state);

    List<Document> findBranches(String code, String state, String district);

    Optional<Document> findBranch(String ifsc);
}
