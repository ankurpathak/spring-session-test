package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.IFileRepository;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.service.IFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class FileService implements IFileService {

    private final IFileRepository fileRepository;

    public FileService(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public String store(InputStream is, String fileName, String mime, Map<String, String> meta) {
        return fileRepository.store(is, fileName, mime, meta);
    }

    @Override
    public String store(InputStream is, String fileName, String mime) {
        return store(is, fileName, mime, Collections.emptyMap());
    }

    @Override
    public String store(MultipartFile file, Map<String, String> meta) {
        return fileRepository.store(file,  meta);
    }

    @Override
    public String store(MultipartFile file) {
        return store(file, Collections.emptyMap());
    }

    @Override
    public Optional<FileContext> findById(String id) {
        return fileRepository.findById(id);
    }
}
