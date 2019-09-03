package com.github.ankurpathak.api.service;

import java.io.IOException;
import java.util.Collection;

public interface ISchemaService {
    void createViews() throws IOException;

    void createCollections() throws IOException;

    Collection<Class<?>> getCollections();
}
