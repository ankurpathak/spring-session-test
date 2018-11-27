package com.ankurpathak.springsessiontest;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;

public class ApplicationExceptionProcessor {
    public static FoundException processDuplicateKeyException(DuplicateKeyException ex, DomainDto<?, ?> dto){
        ensure(ex, notNullValue());
        ensure(dto, notNullValue());
        String message  = ex.getMessage();
        BindingResult result = new BindException(dto, dto.domainName());
        FoundException fEx = null;
        if(!StringUtils.isEmpty(message)){
            if(message.contains(Documents.Index.USER_EMAIL_IDX)){
                UserDto userDto = (UserDto) dto;
                fEx = new FoundException(ex, result, userDto.getEmail(), "email", "User", ApiCode.EMAIL_FOUND);
            }
            else if(message.contains(Documents.Index.USER_CONTACT_IDX)) {
                UserDto userDto = (UserDto) dto;
                fEx = new FoundException(ex, result, userDto.getContact(), "contact", "User", ApiCode.CONTACT_FOUND);
            }

            else if(message.contains(Documents.Index.USER_USERNAME_IDX)){
                UserDto userDto = (UserDto) dto;
                fEx = new FoundException(ex, result, userDto.getUsername(), "username", "User", ApiCode.USERNAME_FOUND);
            }
        }
        if(fEx != null){
            processFoundException(fEx);
            return fEx;
        }
        return null;
    }


    public static void processFoundException(FoundException ex){
        ex.getBindingResult().rejectValue(ex.getProperty(), "Found", "");
    }
}
