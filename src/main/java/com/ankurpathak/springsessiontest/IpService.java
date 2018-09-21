package com.ankurpathak.springsessiontest;

import java.util.Optional;

public interface IpService {
    Optional<String> ipToCountryAlphaCode(String ip);
}
