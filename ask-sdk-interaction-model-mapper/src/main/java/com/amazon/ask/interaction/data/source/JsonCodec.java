package com.amazon.ask.interaction.data.source;

import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads a JSON resource
 */
public class JsonCodec<T> implements Codec<T> {
    private final ObjectReader reader;

    public JsonCodec(ObjectReader reader) {
        this.reader = reader;
    }

    @Override
    public T read(InputStream stream) throws IOException {
        return reader.readValue(stream);
    }
}
