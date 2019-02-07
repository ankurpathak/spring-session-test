package com.github.ankurpathak.app;

import java.util.Optional;

public interface IpService {
    Optional<String> ipToCountryAlphaCode(String ip);
}
