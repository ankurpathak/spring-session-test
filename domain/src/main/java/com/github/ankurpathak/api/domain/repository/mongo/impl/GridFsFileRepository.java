package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.repository.IFileRepository;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Map;

//@Repository
public class GridFsFileRepository implements IFileRepository {
    private final GridFsTemplate gridFsTemplate;

    public GridFsFileRepository(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }


    @Override
    public String store(InputStream is, String fileName, String mime, Map<String, String> meta){
        return gridFsTemplate.store(is, fileName, mime, meta).toString();
    }


    @Override
    public FileContext findById(String id){
        GridFSFile gridFile = gridFsTemplate.findOne(new Query().addCriteria(Criteria.where(Model.Domain.Field.ID).is(id)));
        return null;
    }

}
