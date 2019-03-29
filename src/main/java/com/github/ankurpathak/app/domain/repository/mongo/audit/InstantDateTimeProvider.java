package com.github.ankurpathak.app.domain.repository.mongo.audit;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component
public class InstantDateTimeProvider implements DateTimeProvider {
    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(Instant.now());
    }
}
