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
