package com.github.ankurpathak.app.service;

import java.util.Optional;

public interface IGoogleService extends ISocialService {

    Optional<String> idToken(String code);

}
