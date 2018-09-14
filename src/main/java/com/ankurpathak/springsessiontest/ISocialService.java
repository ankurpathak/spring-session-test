package com.ankurpathak.springsessiontest;

import java.util.Optional;

public interface ISocialService {

    String GOOGLE = "google";
    String FACEBOOK = "facebook";
    String LINKEDIN = "linkedin";


    Optional<String> accessToken(String code);

    Optional<SocialProfile> socialProfile(String token);

    Optional<String> authorizationUrl();
}
