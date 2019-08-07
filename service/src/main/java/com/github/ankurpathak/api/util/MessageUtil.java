package com.github.ankurpathak.api.util;

import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.util.SecurityUtil;
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
        return WebUtil.getLocale();
    }
}
