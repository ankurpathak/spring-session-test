package com.github.ankurpathak.api.domain.repository.mongo;

import java.io.IOException;
import java.util.Collection;

public interface ISchemaRepository {
    void createViews() throws IOException;
    void createCollections() throws IOException;

    Collection<Class<?>> getCollections();
}
