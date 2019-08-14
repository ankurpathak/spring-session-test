package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.repository.IFileRepository;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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
    public Optional<FileContext> findById(String id) {
        return fileRepository.findById(id);
    }
}
