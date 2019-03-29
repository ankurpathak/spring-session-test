package com.github.ankurpathak.app.service;

import java.util.List;

public interface ICountryService {
    List<String> alphaCodeToCallingCodes(String alpha2Code);
}
