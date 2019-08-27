package com.github.ankurpathak.api.domain.repository;

import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface IFileRepository {
    String store(InputStream is, String fileName, String mime, Map<String, String> meta);

    Optional<FileContext> findById(String id);

    String store(MultipartFile file, Map<String, String> meta);
}
