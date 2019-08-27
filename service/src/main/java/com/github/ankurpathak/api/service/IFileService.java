package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface IFileService {
    String store(InputStream is, String fileName, String mime, Map<String, String> meta);

    String store(InputStream is, String fileName, String mime);

    String store(MultipartFile file, Map<String, String> meta);

    String store(MultipartFile file);

    Optional<FileContext> findById(String id);
}
