/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.definition;

import com.amazon.ask.interaction.model.LanguageModel;
import com.fasterxml.jackson.databind.JavaType;

import java.util.*;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;
import static com.amazon.ask.util.ValidationUtils.assertStringNotEmpty;


/**
 * Defines an intent.
 */
public class IntentDefinition {
    private final JavaType intentType;
    private final String name;
    private final boolean isCustom;
    private final Map<String, SlotTypeDefinition> slots;

    /**
     * @param intentType type of intent
     * @param name intent value as defined in the {@link LanguageModel}
     * @param isCustom is this intent custom or built-in (provided by Alexa)
     * @param slots combined of slot names to their {@link SlotTypeDefinition}
     */
    public IntentDefinition(JavaType intentType, String name, boolean isCustom, Map<String, SlotTypeDefinition> slots) {
        this.intentType = assertNotNull(intentType, "intentType");
        this.name = assertStringNotEmpty(name, "name");
        this.slots = slots == null ? Collections.emptyMap() : new TreeMap<>(slots);
        this.isCustom = isCustom;
    }

    public JavaType getIntentType() {
        return intentType;
    }

    public String getName() {
        return name;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public Map<String, SlotTypeDefinition> getSlots() {
        return slots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntentDefinition that = (IntentDefinition) o;
        return isCustom == that.isCustom &&
            Objects.equals(intentType, that.intentType) &&
            Objects.equals(name, that.name) &&
            Objects.equals(slots, that.slots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intentType, name, isCustom, slots);
    }

    @Override
    public String toString() {
        return "IntentDefinition{" +
            "intentType=" + intentType +
            ", name='" + name + '\'' +
            ", isCustom=" + isCustom +
            ", slots=" + slots +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private JavaType intentType;
        private String name;
        private boolean isCustom;
        private Map<String, SlotTypeDefinition> slots;

        public Builder withIntentType(JavaType intentType) {
            this.intentType = intentType;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCustom(boolean isCustom) {
            this.isCustom = isCustom;
            return this;
        }

        public Builder withSlots(Map<String, SlotTypeDefinition> slots) {
            this.slots = new HashMap<>(slots);
            return this;
        }

        public IntentDefinition build() {
            return new IntentDefinition(intentType, name, isCustom, slots);
        }
    }
}
