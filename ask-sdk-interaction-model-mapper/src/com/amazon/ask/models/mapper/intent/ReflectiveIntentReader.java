package com.amazon.ask.models.mapper.intent;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.models.TypeReflector;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.amazon.ask.models.mapper.IntentParseException;

import java.beans.PropertyDescriptor;
import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * TODO
 *
 * Generic parser for a Java Bean where each of its properties have {@link SlotTypeDefinition} associations.
 *
 * Missing slots are ignored and left as initialized.
 *
 * Any reflection errors are translated to an IllegalStateException - accountability for a valid
 * state is delegated to the user.
 */
public class ReflectiveIntentReader<T> implements IntentReader<T> {
    private final TypeReflector<T> reflector;
    private final Map<String, IntentPropertyReader<?>> readers;

    /**
     * @param reflector introspects the intent class
     * @param readers bean property readers
     */
    public ReflectiveIntentReader(TypeReflector<T> reflector, Map<String, IntentPropertyReader<?>> readers) {
        this.reflector = assertNotNull(reflector, "reflector");
        this.readers = assertNotNull(readers, "readers");
    }

    @Override
    public T read(IntentRequest intentRequest) throws IntentParseException {
        try {
            T instance = reflector.getTypeClass().newInstance();
            for (Map.Entry<String, IntentPropertyReader<?>> reader : readers.entrySet()) {
                PropertyDescriptor property = reflector.getPropertyDescriptorIndex().get(reader.getKey());
                if (property == null) {
                    throw new IntentParseException(String.format("Property '%s' does not exist on class '%s'", reader.getKey(), reflector.getTypeClass().getName()));
                }

                Object value = reader.getValue().read(new IntentPropertyContext(intentRequest, reflector, property));
                reflector.set(instance, reader.getKey(), value);
            }
            return instance;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(String.format("Failed to instantiate intent '%s'", reflector.getTypeClass().getName()), ex);
        }
    }
}
