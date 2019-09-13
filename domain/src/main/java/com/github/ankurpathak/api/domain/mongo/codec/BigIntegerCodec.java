package com.github.ankurpathak.api.domain.mongo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigIntegerCodec implements Codec<BigInteger> {
    @Override
    public BigInteger decode(BsonReader reader, DecoderContext decoderContext) {
        return reader.readDecimal128().bigDecimalValue().toBigInteger();
    }

    @Override
    public void encode(BsonWriter writer, BigInteger value, EncoderContext encoderContext) {
        writer.writeDecimal128(new Decimal128(new BigDecimal(value)));
    }

    @Override
    public Class<BigInteger> getEncoderClass() {
        return BigInteger.class;
    }
}
