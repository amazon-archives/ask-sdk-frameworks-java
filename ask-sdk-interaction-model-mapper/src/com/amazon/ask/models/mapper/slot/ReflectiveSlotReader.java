package com.amazon.ask.models.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.models.TypeReflector;
import com.amazon.ask.models.mapper.SlotValueParseException;

import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class ReflectiveSlotReader<T> implements SlotPropertyReader<T> {
    private final TypeReflector<T> reflector;
    private final Map<String, SlotPropertyReader<?>> readers;

    public ReflectiveSlotReader(TypeReflector<T> reflector, Map<String, SlotPropertyReader<?>> readers) {
        this.reflector = assertNotNull(reflector, "reflector");
        this.readers = assertNotNull(readers, "readers");
    }

    @Override
    public T read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        T instance = reflector.instantiate();
        for (Map.Entry<String, SlotPropertyReader<?>> reader : readers.entrySet()) {
            Object value = reader.getValue().read(intentRequest, slot);
            reflector.set(instance, reader.getKey(), value);
        }
        return instance;
    }
}
