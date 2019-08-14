package com.github.ankurpathak.api.domain.repository;

import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface IFileRepository {
    String store(InputStream is, String fileName, String mime, Map<String, String> meta);

    Optional<FileContext> findById(String id);
}
