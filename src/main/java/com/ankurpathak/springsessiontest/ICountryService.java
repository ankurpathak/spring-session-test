package com.ankurpathak.springsessiontest;

import java.util.List;

public interface ICountryService {
    List<String> alphaCodeToCallingCodes(String alpha2Code);
}
