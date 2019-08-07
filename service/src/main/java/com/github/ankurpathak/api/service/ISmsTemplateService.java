package com.github.ankurpathak.api.service;

public interface ISmsTemplateService {
    String createLoginTokenText(String... params);
    String createRegistrationTokenText(String... params);
}
