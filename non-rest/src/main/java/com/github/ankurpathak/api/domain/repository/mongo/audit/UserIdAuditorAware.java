package com.github.ankurpathak.api.domain.repository.mongo.audit;

import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class UserIdAuditorAware implements AuditorAware<BigInteger> {
    @Override
    public Optional<BigInteger> getCurrentAuditor() {
        return TaskContextHolder.getContext().map(ITaskContext::getUser).map(User::getId).or(()-> Optional.of(User.ANONYMOUS_ID));
    }
}
