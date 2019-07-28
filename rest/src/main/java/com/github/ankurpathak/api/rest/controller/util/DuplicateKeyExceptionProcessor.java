package com.github.ankurpathak.api.rest.controller.util;

import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.dto.FoundDto;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import com.mongodb.bulk.BulkWriteError;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class DuplicateKeyExceptionProcessor {
    public static Optional<FoundException> processDuplicateKeyException(DuplicateKeyException ex, Class<?> dtoType){
        require(ex, notNullValue(DuplicateKeyException.class));
        require(dtoType, notNullValue());
        Throwable cause = ex.getCause();
        List<FoundDto> foundDtos = new ArrayList<>();
        if(cause != null){
            if(cause instanceof MongoWriteException){
                processMongoWriteException((MongoWriteException) cause, foundDtos);
            }else if(cause instanceof MongoBulkWriteException){
                processMongoBulkWriteException((MongoBulkWriteException)cause, foundDtos);
            }
        }

        if(!CollectionUtils.isEmpty(foundDtos)){
            return Optional.of(new FoundException(ex, foundDtos, dtoType));
        }

        return Optional.empty();
    }

    private static void processMongoWriteException(MongoWriteException cause, List<FoundDto> dtos) {
        processMongoWriteError(cause.getError(), dtos);
    }

    private static void processMongoBulkWriteException(MongoBulkWriteException cause, List<FoundDto> dtos) {
        List<BulkWriteError> errors = cause.getWriteErrors();
        for (BulkWriteError error: errors){
            processMongoWriteError(error, dtos);
        }
    }

    private static void processMongoWriteError(WriteError error, List<FoundDto> foundDtos) {
        String json = StringUtils.substringAfter(error.getMessage(), "dup key: ").trim();
        String key = StringUtils.substringBetween(json, "{", ":")
                .replace('"',Character.MIN_VALUE)
                .trim();
        key = StringUtils.defaultString(key);
        String value = StringUtils.substringBetween(json, ":", "}")
                .replace('"',Character.MIN_VALUE)
                .trim();
        value = StringUtils.defaultString(value);
        if(StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(key))
            foundDtos.add(new FoundDto(key, value));
    }

}
