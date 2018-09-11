/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.definition.IntentDefinition;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;

import java.util.Locale;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Input to the {@link Compiler}, associating locale with the entity being constructed.
 */
public class RenderContext<T> {
    private final Locale locale;
    private final T value;

    public RenderContext(Locale locale, T value) {
        this.locale = assertNotNull(locale, "locale");
        this.value = assertNotNull(value, "value");
    }

    public Locale getLocale() {
        return locale;
    }

    public T getValue() {
        return value;
    }

    public static Builder<IntentDefinition> intent() {
        return builder();
    }

    public static Builder<SlotTypeDefinition> slotType() {
        return builder();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private Locale locale;
        private T value;

        private Builder() {
        }

        public Builder<T> withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder<T> withValue(T value) {
            this.value = value;
            return this;
        }

        public RenderContext<T> build() {
            return new RenderContext<>(locale, value);
        }
    }
}
