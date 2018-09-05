/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.interaction.TypeReflector;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.mapper.IntentParseException;

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
