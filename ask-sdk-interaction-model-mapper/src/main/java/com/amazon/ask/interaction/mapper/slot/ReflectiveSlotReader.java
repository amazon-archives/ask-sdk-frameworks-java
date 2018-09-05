/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.TypeReflector;
import com.amazon.ask.interaction.mapper.SlotValueParseException;

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
