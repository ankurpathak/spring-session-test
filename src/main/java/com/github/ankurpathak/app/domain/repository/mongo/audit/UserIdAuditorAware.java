package com.github.ankurpathak.app.domain.repository.mongo.audit;

import com.github.ankurpathak.app.security.util.SecurityUtil;
import com.github.ankurpathak.app.domain.model.User;
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
