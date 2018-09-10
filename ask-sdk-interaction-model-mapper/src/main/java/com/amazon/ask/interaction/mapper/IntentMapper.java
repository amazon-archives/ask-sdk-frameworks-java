/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.mapper;


import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentConfirmationStatus;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.SlotConfirmationStatus;
import com.amazon.ask.interaction.TypeReflector;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.annotation.data.AlexaIgnore;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.mapper.intent.*;
import com.amazon.ask.interaction.mapper.slot.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.amazon.ask.model.slu.entityresolution.Resolutions;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Automatically parse values from a {@link IntentRequest}.
 */
@SuppressWarnings("unchecked")
public class IntentMapper {
    private static final Map<ClassKey, IntentPropertyReader> DEFAULT_INTENT_PROPERTY_READERS = new HashMap<>();
    static {
        DEFAULT_INTENT_PROPERTY_READERS.put(new ClassKey(DialogState.class), new DialogStateReader());
        DEFAULT_INTENT_PROPERTY_READERS.put(new ClassKey(IntentConfirmationStatus.class), new IntentConfirmationStatusReader());
    }

    private static final Map<ClassKey, SlotPropertyReader> DEFAULT_SLOT_PROPERTY_READERS = new HashMap<>();
    static {
        DEFAULT_SLOT_PROPERTY_READERS.put(new ClassKey(Slot.class), new RawSlotPropertyReader());
        DEFAULT_SLOT_PROPERTY_READERS.put(new ClassKey(Resolutions.class), new ResolutionsReader());
        DEFAULT_SLOT_PROPERTY_READERS.put(new ClassKey(SlotConfirmationStatus.class), new SlotConfirmationStatusReader());
    }

    private final Model model;

    private final Map<JavaType, IntentReader<?>> intentReaderCache;
    private final Map<ClassKey, SlotPropertyReader> slotReaderCache;

    private final Map<ClassKey, IntentPropertyReader> intentPropertyReaders;
    private final Map<ClassKey, SlotPropertyReader> slotPropertyReaders;

    protected IntentMapper(Model model,
                           Map<ClassKey, IntentPropertyReader> intentPropertyReaders,
                           Map<ClassKey, SlotPropertyReader> slotPropertyReaders) {
        this.model = assertNotNull(model, "model");
        this.intentPropertyReaders = Collections.unmodifiableMap(assertNotNull(intentPropertyReaders, "intentPropertyReaders"));
        this.slotPropertyReaders = Collections.unmodifiableMap(assertNotNull(slotPropertyReaders, "slotPropertyReaders"));

        this.intentReaderCache = new ConcurrentHashMap<>();
        this.slotReaderCache = new ConcurrentHashMap<>();
    }

    /**
     * Parse an intent request into an instance of the class it was registered with in the {@link Model}
     *
     * @param intentRequest intent request
     * @return parsed intent instance
     * @throws IntentParseException if the intent request could not be parsed
     */
    public Object parseIntent(IntentRequest intentRequest) throws IntentParseException {
        return intentReaderFor(getIntentDefinition(intentRequest).getIntentType()).read(intentRequest);
    }

    /**
     * Parse a slot value into an instance of the class it was registered with in the {@link Model}
     *
     * @param intentRequest intent request
     * @param slotName name of the slot
     * @return parsed slot instance
     * @throws IntentParseException if the intent request's slot could not be parsed
     */
    public Object parseIntentSlot(IntentRequest intentRequest, String slotName) throws IntentParseException {
        SlotTypeDefinition slotTypeDefinition = getIntentDefinition(intentRequest).getSlots().get(slotName);
        if (slotTypeDefinition == null) {
            throw new UnrecognizedSlotException(intentRequest, slotName);
        }
        return parseIntentSlot(intentRequest, slotName, slotTypeDefinition.getSlotTypeClass());
    }

    protected IntentDefinition getIntentDefinition(IntentRequest intentRequest) throws UnrecognizedIntentException {
        IntentDefinition intentDefinition = model.getIntentDefinitions().get(intentRequest.getIntent().getName());
        if (intentDefinition == null) {
            throw new UnrecognizedIntentException(intentRequest);
        }
        return intentDefinition;
    }

    /**
     * Parse a slot value from an {@link IntentRequest} the slot type known.
     *
     * The slot class to parse into is arbitrary:
     * - It does not need to match the class registered with {@link Model.Builder#intent(Class)} or {@link Model.Builder#slotType(Class)}
     * - It does not need a {@link SlotType} annotation
     *
     * @param intentRequest intent request
     * @param slotName  name of the slot to read from intent
     * @param slotClass class to parse from the slot
     * @param <T> type of the slot
     * @return parsed slot instance
     * @throws IntentParseException if an instance could not be parsed from the intent request's slot
     */
    public <T> T parseIntentSlot(IntentRequest intentRequest, String slotName, Class<T> slotClass) throws IntentParseException {
        try {
            Slot slot = intentRequest.getIntent().getSlots().get(slotName);
            if (slot == null) {
                throw new UnrecognizedSlotException(intentRequest, slotName);
            }
            if (slot.getValue() == null) {
                return null;
            }
            return slotClass.cast(slotReaderFor(slotClass).read(intentRequest, slot));
        } catch (ClassCastException ex) {
            throw new IntentParseException("Failed to read intent", ex);
        }
    }

    /**
     * Parse the {@link IntentRequest} into a known type.
     *
     * The intent class to parse into is arbitrary:
     * - It does not need to match the class registered with {@link Model.Builder#intent(Class)}
     * - It does not need an {@link Intent} annotation
     *
     * @param intentRequest intent request
     * @param intentClass type of class to parse intent request into
     * @param <T> type of intent
     * @return instance
     * @throws IntentParseException if an instance could not be parsed from the request
     */
    public <T> T parseIntent(IntentRequest intentRequest, Class<T> intentClass) throws IntentParseException {
        return intentReaderFor(intentClass).read(intentRequest);
    }

    public <T> IntentReader<T> intentReaderFor(Class<T> intentClass, Class<?>... typeParameters) {
        return intentReaderFor(TypeFactory.defaultInstance().constructParametricType(intentClass, typeParameters));
    }

    public <T> IntentReader<T> intentReaderFor(Class<T> intentClass) {
        return intentReaderFor(TypeFactory.defaultInstance().constructSimpleType(intentClass, new JavaType[]{}));
    }

    public <T> IntentReader<T> intentReaderFor(JavaType type) {
        return (IntentReader<T>) intentReaderCache.computeIfAbsent(type, k -> {
            TypeReflector<T> reflector = new TypeReflector<>(type);

            Map<String, IntentPropertyReader<?>> intentPropertyReaders = new HashMap<>();
            Set<String> slotNames = new HashSet<>(); // detect duplicate slot names
            for (PropertyDescriptor prop : reflector.getPropertyDescriptors()) {
                if (reflector.getAnnotation(prop, AlexaIgnore.class) != null) {
                    continue;
                }

                Class<?> propertyType = prop.getPropertyType() != Object.class
                    ? prop.getPropertyType()
                    : reflector.reifyPropertyType(prop);

                IntentPropertyReader reader;
                com.amazon.ask.interaction.annotation.data.IntentPropertyReader intentPropertyReader = reflector.getAnnotation(prop, com.amazon.ask.interaction.annotation.data.IntentPropertyReader.class);
                if (intentPropertyReader == null) {
                    intentPropertyReader = Utils.findAnnotation(propertyType, com.amazon.ask.interaction.annotation.data.IntentPropertyReader.class);
                }
                if (intentPropertyReader != null) {
                    reader = Utils.instantiate(intentPropertyReader.value());
                } else {
                    // TODO: Should we check this before the type's annotation?
                    reader = this.intentPropertyReaders.get(new ClassKey(propertyType));
                }

                // Slot properties must be annotated with @AlexaSlot
                SlotProperty slotProperty = reflector.getAnnotation(prop, SlotProperty.class);
                if (slotProperty != null) {
                    String slotName = slotProperty.name().isEmpty() ? prop.getName() : slotProperty.name();
                    if (slotNames.contains(slotName)) {
                        throw new IllegalArgumentException(String.format("Slot '%s' declared multiple times on intent %s", slotName, type.getRawClass().getName()));
                    }
                    slotNames.add(slotName);
                    if (reader == null) {
                        SlotPropertyReader slotPropertyReader;
                        com.amazon.ask.interaction.annotation.data.SlotPropertyReader alexaSlotPropertyReader = reflector.getAnnotation(prop, com.amazon.ask.interaction.annotation.data.SlotPropertyReader.class);
                        if (alexaSlotPropertyReader != null) {
                            slotPropertyReader =  Utils.instantiate(alexaSlotPropertyReader.value());
                        } else {
                            slotPropertyReader = slotReaderFor(propertyType);
                        }
                        reader = new IntentSlotPropertyReader(slotPropertyReader,slotName);
                    }
                }

                if (reader == null) {
                    throw new IllegalArgumentException(String.format("Could not resolve an %s for property '%s' on intent %s", IntentPropertyReader.class.getName(), prop.getName(), type.getRawClass().getName()));
                }

                intentPropertyReaders.put(prop.getName(), reader);
            }

            return new ReflectiveIntentReader<T>(reflector, intentPropertyReaders);
        });
    }

    public <T> SlotPropertyReader slotReaderFor(Class<T> slotClass) {
        return this.slotReaderCache.computeIfAbsent(new ClassKey(slotClass), k -> {
            com.amazon.ask.interaction.annotation.data.SlotPropertyReader slotPropertyReader = Utils.findAnnotation(slotClass, com.amazon.ask.interaction.annotation.data.SlotPropertyReader.class);
            if (slotPropertyReader != null) {
                return Utils.instantiate(slotPropertyReader.value());
            }
            if (slotClass.isEnum()) {
                return new EnumCustomSlotReader(slotClass);
            }
            ClassKey classKey = new ClassKey(slotClass);
            if (this.slotPropertyReaders.containsKey(classKey)) {
                return this.slotPropertyReaders.get(classKey);
            }

            TypeReflector<?> slotTypeReflector = new TypeReflector<>(slotClass);
            Map<String, SlotPropertyReader> readers = new HashMap<>();
            for (PropertyDescriptor slotProperty : slotTypeReflector.getPropertyDescriptors()) {
                if (slotTypeReflector.getAnnotation(slotProperty, AlexaIgnore.class) != null) {
                    continue;
                }

                readers.put(slotProperty.getName(), resolveSlotPropertyReader(slotTypeReflector, slotProperty));
            }
            return new ReflectiveSlotReader(slotTypeReflector, readers);
        });
    }

    protected SlotPropertyReader resolveSlotPropertyReader(TypeReflector<?> slotTypeReflector, PropertyDescriptor slotProperty) {
        Class<?> propertyType = slotProperty.getPropertyType();
        if (propertyType == Object.class) {
            propertyType = slotTypeReflector.reifyPropertyType(slotProperty);
        }

        SlotPropertyReader reader;
        com.amazon.ask.interaction.annotation.data.SlotPropertyReader attribute = slotTypeReflector.getAnnotation(slotProperty, com.amazon.ask.interaction.annotation.data.SlotPropertyReader.class);
        if (attribute == null) {
            attribute = Utils.findAnnotation(propertyType, com.amazon.ask.interaction.annotation.data.SlotPropertyReader.class);
        }
        if (attribute != null) {
            return Utils.instantiate(attribute.value());
        } else {
            reader = slotPropertyReaders.get(new ClassKey(propertyType));
        }

        if (reader == null) {
            throw new IllegalArgumentException(String.format("Could not resolve a %s for %s", SlotPropertyReader.class.getName(), propertyType.getName()));
        }

        return reader;
    }

    public static IntentMapper fromModel(Model model) {
        return builder().withModel(model).build();
    }

    public static Builder builder() {
        return new Builder()
            .addIntentPropertyReaders(DEFAULT_INTENT_PROPERTY_READERS)
            .addSlotPropertyReaders(DEFAULT_SLOT_PROPERTY_READERS);
    }

    public static final class Builder {
        private Model model;
        private Map<ClassKey, IntentPropertyReader> intentPropertyReaders = new HashMap<>();
        private Map<ClassKey, SlotPropertyReader> slotPropertyReaders = new HashMap<>();

        private Builder() {
        }

        public Builder withSkillModel(SkillModel model) {
            this.model = model.getModel();
            return this;
        }

        public Builder withModel(Model model) {
            this.model = model;
            return this;
        }

        public Builder withIntentPropertyReaders(Map<ClassKey, IntentPropertyReader> intentPropertyReaders) {
            this.intentPropertyReaders = intentPropertyReaders;
            return this;
        }

        public Builder addIntentPropertyReaders(Map<ClassKey, IntentPropertyReader> intentPropertyReaders) {
            intentPropertyReaders.forEach(this::addIntentPropertyReader);
            return this;
        }

        public Builder addIntentPropertyReader(ClassKey classKey, IntentPropertyReader intentPropertyReader) {
            if (this.intentPropertyReaders == null) {
                this.intentPropertyReaders = new HashMap<>();
            }
            this.intentPropertyReaders.put(classKey, intentPropertyReader);
            return this;
        }

        public Builder withSlotPropertyReaders(Map<ClassKey, SlotPropertyReader> slotPropertyReaders) {
            this.slotPropertyReaders = slotPropertyReaders;
            return this;
        }

        public Builder addSlotPropertyReaders(Map<ClassKey, SlotPropertyReader> slotPropertyReaders) {
            slotPropertyReaders.forEach(this::addSlotPropertyReader);
            return this;
        }

        public Builder addSlotPropertyReader(ClassKey classKey, SlotPropertyReader slotPropertyReader) {
            if (this.slotPropertyReaders == null) {
                this.slotPropertyReaders = new HashMap<>();
            }
            this.slotPropertyReaders.put(classKey, slotPropertyReader);
            return this;
        }

        public IntentMapper build() {
            return new IntentMapper(model, intentPropertyReaders, slotPropertyReaders);
        }
    }
}
