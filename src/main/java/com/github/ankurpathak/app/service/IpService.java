package com.github.ankurpathak.app.service;

import java.util.Optional;

public interface IpService {
    Optional<String> ipToCountryAlphaCode(String ip);
}