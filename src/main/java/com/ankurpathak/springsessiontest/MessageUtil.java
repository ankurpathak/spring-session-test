package com.ankurpathak.springsessiontest;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;

public class MessageUtil {
    public static final String getMessage(MessageSource messageSource, String key){
        Optional<DomainContext> context = SecurityUtil.getDomainContext();
        if(context.isPresent()){
            return messageSource.getMessage(key, null, "", context.get().getLocale());
        }else {
            return messageSource.getMessage(key, null, "", Locale.getDefault());
        }
    }


    public static final String getMessage(MessageSource messageSource, String key, String... args){
        Optional<DomainContext> context = SecurityUtil.getDomainContext();
        if(context.isPresent()){
            return messageSource.getMessage(key, args, "", context.get().getLocale());
        }else {
            return messageSource.getMessage(key, args, "", Locale.getDefault());
        }
    }
}
