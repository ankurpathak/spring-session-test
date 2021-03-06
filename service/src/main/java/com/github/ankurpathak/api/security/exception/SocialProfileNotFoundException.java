package com.github.ankurpathak.api.security.exception;

import com.github.ankurpathak.api.service.dto.SocialProfile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SocialProfileNotFoundException extends UsernameNotFoundException {

    private final SocialProfile profile;

    public SocialProfileNotFoundException(String msg, SocialProfile profile) {
        super(msg);
        this.profile = profile;
    }

    public SocialProfileNotFoundException(String msg, Throwable ex, SocialProfile profile) {
        super(msg, ex);
        this.profile = profile;
    }

    public SocialProfile getProfile() {
        return profile;
    }
}
