package com.github.ankurpathak.api.domain.repository.mongo;

import java.io.IOException;

public interface ICountryEtlRepository {
    void process() throws IOException;
}
