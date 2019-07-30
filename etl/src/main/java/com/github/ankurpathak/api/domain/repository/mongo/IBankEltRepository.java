package com.github.ankurpathak.api.domain.repository.mongo;

import java.io.IOException;

public interface IBankEltRepository {
    void process() throws IOException;
}
