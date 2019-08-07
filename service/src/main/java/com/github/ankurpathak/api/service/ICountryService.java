package com.github.ankurpathak.api.service;

import java.util.List;

public interface ICountryService {
    List<String> alphaCodeToCallingCodes(String alpha2Code);
}
