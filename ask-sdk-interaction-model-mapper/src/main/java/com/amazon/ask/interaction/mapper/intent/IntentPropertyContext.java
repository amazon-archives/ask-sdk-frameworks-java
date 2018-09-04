package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.interaction.TypeReflector;

import java.beans.PropertyDescriptor;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class IntentPropertyContext {
    private final IntentRequest intentRequest;
    private final TypeReflector<?> typeReflector;
    private final PropertyDescriptor propertyDescriptor;

    public IntentPropertyContext(IntentRequest intentRequest, TypeReflector<?> typeReflector, PropertyDescriptor propertyDescriptor) {
        this.intentRequest = assertNotNull(intentRequest, "intentRequest");
        this.typeReflector = assertNotNull(typeReflector, "typeReflector");
        this.propertyDescriptor = assertNotNull(propertyDescriptor, "propertyDescriptor");
    }

    public IntentRequest getIntentRequest() {
        return intentRequest;
    }

    public TypeReflector<?> getTypeReflector() {
        return typeReflector;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private IntentRequest intentRequest;
        private TypeReflector<?> typeReflector;
        private PropertyDescriptor propertyDescriptor;

        private Builder() {
        }

        public Builder withIntentRequest(IntentRequest intentRequest) {
            this.intentRequest = intentRequest;
            return this;
        }

        public Builder withTypeReflector(TypeReflector<?> typeReflector) {
            this.typeReflector = typeReflector;
            return this;
        }

        public Builder withPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
            this.propertyDescriptor = propertyDescriptor;
            return this;
        }

        public IntentPropertyContext build() {
            return new IntentPropertyContext(intentRequest, typeReflector, propertyDescriptor);
        }
    }
}
