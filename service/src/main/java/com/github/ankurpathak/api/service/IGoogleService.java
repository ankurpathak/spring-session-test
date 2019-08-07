package com.github.ankurpathak.api.service;

import java.util.Optional;

public interface IGoogleService extends ISocialService {

    Optional<String> idToken(String code);

}
