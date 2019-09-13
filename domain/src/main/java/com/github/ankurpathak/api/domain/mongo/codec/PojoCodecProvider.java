package com.github.ankurpathak.api.domain.mongo.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Task;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.List;

public class PojoCodecProvider implements CodecProvider {
    private final Codec<Document> documentCodec;
    private final ObjectMapper objectMapper;

    public PojoCodecProvider(Codec<Document> documentCodec, ObjectMapper objectMapper) {
        this.documentCodec = documentCodec;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry registry) {
        if(List.of(Task.class, User.class, Business.class, FileContext.class).contains(type))
            return (Codec<T>) new PojoCodec(documentCodec, objectMapper);
        else
            return null;
    }
}
