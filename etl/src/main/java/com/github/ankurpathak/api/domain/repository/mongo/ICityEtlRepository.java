package com.github.ankurpathak.api.domain.repository.mongo;

import java.io.IOException;

public interface ICityEtlRepository {
    void process() throws IOException;
}
