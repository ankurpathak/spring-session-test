package com.github.ankurpathak.app.service;

import org.springframework.validation.FieldError;

public interface IMessageService {

    String getMessage(String key);
    String getMessage(String key, String... args);
    String getMessage(FieldError error);
}
