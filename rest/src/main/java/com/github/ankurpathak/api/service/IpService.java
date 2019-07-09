package com.github.ankurpathak.api.service;

import java.util.Optional;

public interface IpService {
    Optional<String> ipToCountryAlphaCode(String ip);
}
