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
