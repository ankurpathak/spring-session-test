package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.repository.IFileRepository;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.exception.ServiceException;
import com.github.ankurpathak.api.util.LogUtil;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Repository
public class GridFsFileRepository implements IFileRepository {

    private static final Logger log = LoggerFactory.getLogger(GridFsFileRepository.class);


    private final GridFsTemplate gridFsTemplate;

    public GridFsFileRepository(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }


    @Override
    public String store(InputStream is, String fileName, String mime, Map<String, String> meta){
        return gridFsTemplate.store(is, fileName, mime, meta).toString();
    }


    @Override
    @SuppressWarnings("deprecation")
    public Optional<FileContext> findById(String id){
        Optional<GridFSFile> gridFile = Optional.ofNullable(gridFsTemplate.findOne(new Query().addCriteria(Criteria.where(Model.Domain.Field.ID).is(id))));
        if(gridFile.isPresent()){
            try{
                return Optional.of(FileContext.getInstance()
                        .meta((Map)gridFile.get().getMetadata())
                        .mime(gridFile.get().getContentType())
                        .fileName(gridFile.get().getFilename())
                        .path(gridFsTemplate.getResource(gridFile.get()).getFile().toPath()));
            }catch (IOException ex){
                LogUtil.logStackTrace(log, ex);
                throw new ServiceException(ex.getMessage(), ex);
            }
        }else {
            return Optional.empty();
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
