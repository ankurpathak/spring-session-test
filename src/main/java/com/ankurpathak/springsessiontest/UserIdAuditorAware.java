package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class UserIdAuditorAware implements AuditorAware<BigInteger> {
    @Override
    public Optional<BigInteger> getCurrentAuditor() {
        return SecurityUtil.getMe().map(User::getId).or(()-> Optional.of(User.ANONYMOUS_ID));
    }
}
