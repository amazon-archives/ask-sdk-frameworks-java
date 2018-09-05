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


import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;
import static com.amazon.ask.util.ValidationUtils.assertStringNotEmpty;


/**
 * Defines a slot type's schema
 */
public class SlotTypeDefinition {
    private final Class<?> slotTypeClass;
    private final String name;
    private final boolean isCustom;

    public SlotTypeDefinition(Class<?> slotTypeClass, String name, boolean isCustom) {
        this.slotTypeClass = assertNotNull(slotTypeClass, "slotTypeClass");
        this.name = assertStringNotEmpty(name, "name");
        this.isCustom = isCustom;
    }

    public Class<?> getSlotTypeClass() {
        return slotTypeClass;
    }

    public String getName() {
        return name;
    }

    public boolean isCustom() {
        return isCustom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotTypeDefinition that = (SlotTypeDefinition) o;
        return isCustom == that.isCustom &&
            Objects.equals(slotTypeClass, that.slotTypeClass) &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotTypeClass, name, isCustom);
    }

    @Override
    public String toString() {
        return "SlotTypeDefinition{" +
            "slotTypeClass=" + slotTypeClass +
            ", name='" + name + '\'' +
            ", isCustom=" + isCustom +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Class<?> slotTypeClass;
        private String name;
        private Boolean isCustom;

        public Builder withSlotTypeClass(Class<?> slotTypeClass) {
            this.slotTypeClass = slotTypeClass;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCustom(boolean custom) {
            isCustom = custom;
            return this;
        }

        public SlotTypeDefinition build() {
            return new SlotTypeDefinition(slotTypeClass, name, isCustom == null ? true : isCustom);
        }
    }
}
