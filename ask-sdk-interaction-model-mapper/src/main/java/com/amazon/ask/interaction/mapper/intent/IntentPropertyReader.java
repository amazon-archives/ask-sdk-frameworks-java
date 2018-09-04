package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.interaction.mapper.IntentParseException;

/**
 *
 */
public interface IntentPropertyReader<T> {
    T read(IntentPropertyContext context) throws IntentParseException;
}
