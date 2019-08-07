package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.service.dto.SocialProfile;

import java.util.Optional;

public interface ISocialService {

    String GOOGLE = "google";
    String FACEBOOK = "facebook";
    String LINKEDIN = "linkedin";


    Optional<String> accessToken(String code);

    Optional<SocialProfile> socialProfile(String token);

    Optional<String> authorizationUrl();
}
