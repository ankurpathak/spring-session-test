package com.github.ankurpathak.app;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.FieldError;

import java.util.Locale;
import java.util.Optional;

public class MessageUtil {
    public static final String getMessage(MessageSource messageSource, String key) {
        return messageSource.getMessage(key, null, "", getLocale());
    }


    public static final String getMessage(MessageSource messageSource, String key, String... args) {
        return messageSource.getMessage(key, args, "", getLocale());
    }



    public static final String getMessage(MessageSource messageSource, FieldError error){
        try {
            return messageSource.getMessage(error, getLocale());
        }catch (NoSuchMessageException ex){
            return "";
        }
    }

    public static final Locale getLocale() {
        Optional<DomainContext> context = SecurityUtil.getDomainContext();
        if (context.isPresent()) {
            return context.get().getLocale();
        } else {
            return Locale.getDefault();
        }


    }
}
