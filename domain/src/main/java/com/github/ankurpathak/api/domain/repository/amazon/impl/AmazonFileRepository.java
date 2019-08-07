package com.github.ankurpathak.api.domain.repository.amazon.impl;

import com.github.ankurpathak.api.constant.AwsConstant;
import com.github.ankurpathak.api.domain.repository.IFileRepository;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Repository
public class AmazonFileRepository implements IFileRepository {
    private final S3Client s3;

    public AmazonFileRepository(S3Client s3) {
        this.s3 = s3;
    }

    @Override
    public String store(InputStream is, String fileName, String mime, Map<String, String> meta)  {
        try{
            String key = String.format("%s-%s", UUID.randomUUID().toString().replaceAll("-",""), fileName);
            Path tempPath = Files.createTempFile(String.format("%s-",FilenameUtils.getBaseName(fileName)), FilenameUtils.getExtension(fileName));
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(AwsConstant.S3.BUCKET)
                    .key(fileName)
                    .metadata(meta)
                    .build();
            s3.putObject(request, tempPath);
            return key;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public FileContext findById(String id) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(AwsConstant.S3.BUCKET)
                .key(id).build();
        try {
            Path tempFile = Files.createTempFile(id, ".temp");
            GetObjectResponse response = s3.getObject(request, tempFile);
            Map<String, String> meta = response.metadata();
            return FileContext.getInstance()
                    .path(tempFile)
                    .mime(response.contentType())
                    .meta(response.metadata());

        }catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

    }
















































































































































}
