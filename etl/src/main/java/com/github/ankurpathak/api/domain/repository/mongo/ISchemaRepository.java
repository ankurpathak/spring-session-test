package com.github.ankurpathak.api.domain.repository.mongo;

import java.io.IOException;

public interface ISchemaRepository {
    void createViews() throws IOException;
    void createCollections() throws IOException;
}
