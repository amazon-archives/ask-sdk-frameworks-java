package com.amazon.ask.models.mapper.intent;

import com.amazon.ask.models.mapper.IntentParseException;

/**
 *
 */
public interface IntentPropertyReader<T> {
    T read(IntentPropertyContext context) throws IntentParseException;
}
