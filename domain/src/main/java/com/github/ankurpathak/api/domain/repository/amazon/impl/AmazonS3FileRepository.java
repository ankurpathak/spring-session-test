package com.github.ankurpathak.api.domain.repository.amazon.impl;

import com.github.ankurpathak.api.constant.AwsConstant;
import com.github.ankurpathak.api.domain.repository.IFileRepository;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.exception.ServiceException;
import com.github.ankurpathak.api.util.LogUtil;
import com.github.ankurpathak.api.util.PathUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class AmazonS3FileRepository implements IFileRepository {
    private static final Logger log = LoggerFactory.getLogger(AmazonS3FileRepository.class);

    private final S3Client s3;

    public AmazonS3FileRepository(S3Client s3) {
        this.s3 = s3;
    }

    @Override
    public String store(InputStream is, String fileName, String mime, Map<String, String> meta)  {
        try{
            String key = String.format("%s-%s", UUID.randomUUID().toString().replaceAll("-",""), fileName);
            Path tempPath = PathUtils.createTempPath(String.format("%s-",FilenameUtils.getBaseName(fileName)), String.format(".%s",FilenameUtils.getExtension(fileName)));
            FileUtils.copyInputStreamToFile(is, tempPath.toFile());
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(AwsConstant.S3.BUCKET)
                    .contentType(mime)
                    .key(key)
                    .metadata(meta)
                    .build();
            s3.putObject(request, tempPath);
            File tempFile = tempPath.toFile();
            if(tempFile.exists()){
                tempFile.delete();
            }
            return key;
        }
        catch (SdkException | IOException ex) {
            LogUtil.logStackTrace(log, ex);
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<FileContext> findById(String id) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(AwsConstant.S3.BUCKET)
                .key(id)
                .build();
        try {
            Path tempFile = PathUtils.createTempPath(id, ".temp");
            GetObjectResponse response = s3.getObject(request, tempFile);
            Map<String, String> meta = response.metadata();
            return Optional.of(FileContext.getInstance()
                    .path(tempFile)
                    .mime(response.contentType())
                    .meta(response.metadata()));

        }catch (NoSuchKeyException noSuchKeyEx) {
            LogUtil.logStackTrace(log, noSuchKeyEx);
            return Optional.empty();
        } catch (SdkException ex) {
            LogUtil.logStackTrace(log, ex);
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public String store(MultipartFile file, Map<String, String> meta) {
        try{
            return store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), meta);
        }catch (IOException ex){
            LogUtil.logStackTrace(log, ex);
            throw new ServiceException(ex.getMessage(), ex);
        }
    }


}
