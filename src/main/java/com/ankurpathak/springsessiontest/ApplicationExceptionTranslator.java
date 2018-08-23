package com.ankurpathak.springsessiontest;

import org.springframework.dao.DuplicateKeyException;

import java.io.Serializable;

public class ApplicationExceptionTranslator {
    public static FoundException convertToFoundException(DuplicateKeyException ex, DomainDto<? extends Serializable> dto){
        if(ex.getMessage().contains(DocumentCollections.Index.USERS_EMAIL_IDX)){
            UserDto userDto = (UserDto) dto;
            return new FoundException(ex,userDto.getEmail(), "email", "User", ApiCode.EMAIL_FOUND);
        }
        else if(ex.getMessage().contains(DocumentCollections.Index.USERS_CONTACT_IDX)) {
            UserDto userDto = (UserDto) dto;
            return new FoundException(ex, userDto.getEmail(), "contact", "User", ApiCode.CONTACT_FOUND);
        }

        else if(ex.getMessage().contains(DocumentCollections.Index.USERS_USERNAME_IDX)){
            UserDto userDto = (UserDto) dto;
            return new FoundException(ex, userDto.getEmail(), "username", "User", ApiCode.USERNAME_FOUND);
        }
        return new FoundException(ex, "NA", "NA", dto.domainName(), ApiCode.FOUND);
    }
}
