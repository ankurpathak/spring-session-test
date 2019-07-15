package com.github.ankurpathak.api.domain.repository.mongo;

import java.io.IOException;

public interface IBankIfscEltRepository {
    void process() throws IOException;
}
