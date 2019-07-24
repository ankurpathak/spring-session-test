package com.github.ankurpathak.api.rest.controller.util;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.dto.FoundDto;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.DomainDto;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import com.mongodb.bulk.BulkWriteError;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.valid4j.Assertive.require;

public class DuplicateKeyExceptionProcessor {
    public static Optional<FoundException> processDuplicateKeyException(DuplicateKeyException ex, DomainDto<?, ?> dto){
        require(ex, notNullValue());
        require(dto, notNullValue());
        Throwable cause = ex.getCause();
        List<FoundDto> foundDtos = new ArrayList<>();
        BindingResult result = new BindException(dto, dto.domainName());
        if(cause != null){
            if(cause instanceof MongoWriteException){
                processMongoWriteException((MongoWriteException) cause, dto, foundDtos);
            }else if(cause instanceof MongoBulkWriteException){
                processMongoBulkWriteException((MongoBulkWriteException)cause, dto, foundDtos);
            }
        }
        if(!CollectionUtils.isEmpty(foundDtos)){
            processFoundDtoList(foundDtos, result);
            return Optional.of(new FoundException(ex, result, foundDtos));
        }
        return Optional.empty();
    }

    private static void processFoundDtoList(List<FoundDto> foundDtos, BindingResult result) {
        for(FoundDto foundDto: foundDtos){
            result.rejectValue(foundDto.getProperty(), "Found", "");
        }

    }

    private static void processMongoBulkWriteException(MongoBulkWriteException cause, DomainDto<?, ?> dto, List<FoundDto> foundDtos) {
        List<BulkWriteError> errors = cause.getWriteErrors();
        for (BulkWriteError error: errors){
            processMongoWriteExceptionErrorMessage(error, dto, foundDtos);
        }
    }

    private static void processMongoWriteException(MongoWriteException cause, DomainDto<?, ?> dto,  List<FoundDto> foundDtos) {
        WriteError error = cause.getError();
        processMongoWriteExceptionErrorMessage(error, dto, foundDtos);
    }

    private static void processMongoWriteExceptionErrorMessage(WriteError error, DomainDto<?, ?> dto, List<FoundDto> foundDtos){
        String message  = error.getMessage();
        if(!StringUtils.isEmpty(message)){
            if(message.contains(Model.User.Index.EMAIL_IDX)){
                UserDto userDto = (UserDto) dto;
                foundDtos.add(new FoundDto("User", "email", userDto.getEmail(), ApiCode.EMAIL_FOUND));
            }
            else if(message.contains(Model.User.Index.PHONE_IDX)) {
                UserDto userDto = (UserDto) dto;
                foundDtos.add(new FoundDto("User", "phone", userDto.getContact(), ApiCode.CONTACT_FOUND));
            } else if(message.contains(Model.User.Index.USERNAME_IDX)){
                UserDto userDto = (UserDto) dto;
                foundDtos.add(new FoundDto("User", "username", userDto.getUsername(), ApiCode.USER_NOT_FOUND));
            }
        }

    }



}
