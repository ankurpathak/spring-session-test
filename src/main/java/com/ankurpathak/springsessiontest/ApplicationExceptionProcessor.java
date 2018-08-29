package com.ankurpathak.springsessiontest;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

public class ApplicationExceptionProcessor {
    public static FoundException processDuplicateKeyException(DuplicateKeyException ex, DomainDto<?, ?> dto, BindingResult result){
        String message  = ex.getMessage();
        FoundException fEx = null;
        if(!StringUtils.isEmpty(message)){
            if(ex.getMessage().contains(DocumentCollections.Index.USERS_EMAIL_IDX)){
                UserDto userDto = (UserDto) dto;
                fEx = new FoundException(ex, result, userDto.getEmail(), "email", "User", ApiCode.EMAIL_FOUND);
            }
            else if(ex.getMessage().contains(DocumentCollections.Index.USERS_CONTACT_IDX)) {
                UserDto userDto = (UserDto) dto;
                fEx = new FoundException(ex, result, userDto.getContact(), "contact", "User", ApiCode.CONTACT_FOUND);
            }

            else if(ex.getMessage().contains(DocumentCollections.Index.USERS_USERNAME_IDX)){
                UserDto userDto = (UserDto) dto;
                fEx = new FoundException(ex, result, userDto.getUsername(), "username", "User", ApiCode.USERNAME_FOUND);
            }
        }
        if(fEx != null){
            processFoundException(fEx);
        }
        return null;
    }


    public static void processFoundException(FoundException ex){
        ex.getBindingResult().rejectValue(ex.getProperty(), "Found", "");
    }
}
