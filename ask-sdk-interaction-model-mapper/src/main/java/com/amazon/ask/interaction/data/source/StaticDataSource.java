/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.data.source;

import com.amazon.ask.interaction.data.model.IntentData;
import com.amazon.ask.interaction.data.model.SlotTypeData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Hard-code interaction model data such as {@link IntentData} and {@link SlotTypeData}
 */
public class StaticDataSource<T> implements Function<Locale, T> {
    private final T defaultData;
    private final Map<Locale, Supplier<T>> data;

    public StaticDataSource(T defaultData, Map<Locale, Supplier<T>> data) {
        this.defaultData = assertNotNull(defaultData, "empty");
        this.data = assertNotNull(data, "data");
    }

    @Override
    public T apply(Locale locale) {
        return Optional
            .ofNullable(data.get(locale))
            .map(Supplier::get)
            .orElse(defaultData);
    }

    public static Builder<IntentData> intent() {
        return builder();
    }

    public static Builder<SlotTypeData> slotType() {
        return builder();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        protected T defaultData;
        protected Map<Locale, Supplier<T>> data;

        public Builder<T> withDefault(T defaultData) {
            this.defaultData = defaultData;
            return this;
        }

        public Builder<T> withData(Map<Locale, T> data) {
            data.forEach(this::withData);
            return this;
        }


        public Builder<T> withData(Locale locale, T data) {
            return withData(locale, () -> data);
        }

        public Builder<T> withData(Locale locale, Supplier<T> data) {
            if (this.data == null) {
                this.data = new HashMap<>();
            }
            this.data.put(locale, data);
            return this;
        }

        public StaticDataSource<T> build() {
            return new StaticDataSource<>(defaultData, data);
        }
    }
}
