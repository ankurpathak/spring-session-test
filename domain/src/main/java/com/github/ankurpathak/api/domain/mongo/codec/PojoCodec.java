package com.github.ankurpathak.api.domain.mongo.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.Map;

public class PojoCodec implements Codec<Object> {
    private final Codec<Document> documentCodec;
    private final ObjectMapper objectMapper;

    public PojoCodec(Codec<Document> documentCodec, ObjectMapper objectMapper) {
        this.documentCodec = documentCodec;
        this.objectMapper = objectMapper;
    }

    @Override
    public Object decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        return objectMapper.convertValue(document, Object.class);
    }

    @Override
    public void encode(BsonWriter bsonWriter, Object object, EncoderContext encoderContext) {
        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        documentCodec.encode(bsonWriter, new Document(map), encoderContext);
    }

    @Override
    public Class<Object> getEncoderClass() {
        return Object.class;
    }
}
