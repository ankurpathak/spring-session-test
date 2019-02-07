package com.github.ankurpathak.app;

import java.util.Optional;

public interface IGoogleService extends ISocialService {

    Optional<String> idToken(String code);

}
