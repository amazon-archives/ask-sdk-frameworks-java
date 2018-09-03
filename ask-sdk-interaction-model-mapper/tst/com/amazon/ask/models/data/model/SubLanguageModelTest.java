package com.amazon.ask.models.data.model;

import com.amazon.ask.interaction.model.Intent;
import com.amazon.ask.interaction.model.SlotType;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 */
public class SubLanguageModelTest {

    @Test
    public void testEqualsSelf() {
        SubLanguageModel test = mockBuilder("test").build();

        assertEquals(test, test);
        assertEquals(test.hashCode(), test.hashCode());
    }

    @Test
    public void testNotEqualsNull() {
        SubLanguageModel test = mockBuilder("test").build();

        assertNotEquals(test, null);
        assertNotEquals(null, test);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        SubLanguageModel test = mockBuilder("test").build();

        assertNotEquals(test, "different");
        assertNotEquals("different", test);
    }

    @Test
    public void testNotEqualsDifferentIntent() {
        SubLanguageModel left = mockBuilder("test").build();
        SubLanguageModel right = mockBuilder("test")
            .withIntentName("different")
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        SubLanguageModel left = mockBuilder("test").build();
        SubLanguageModel right = mockBuilder("test")
            .withSlotName("different")
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    // Helper for constructing instances that slightly vary
    private static Builder mockBuilder(String defaultValue) {
        return new Builder(defaultValue);
    }
    private static final class Builder {
        private final String defaultValue;

        private String intentName;
        private String slotName;

        private Builder(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Builder withIntentName(String intentName) {
            this.intentName = intentName;
            return this;
        }

        public Builder withSlotName(String slotName) {
            this.slotName = slotName;
            return this;
        }

        public SubLanguageModel build() {
            return SubLanguageModel.builder()
                .withIntents(Collections.singletonList(
                    Intent.builder()
                        .withName(resolve(intentName))
                        .build()
                ))
                .withTypes(Collections.singletonList(
                    SlotType.builder()
                        .withName(resolve(slotName))
                        .build()
                ))
                .build();
        }

        private String resolve(String setValue) {
            return setValue == null ? defaultValue : setValue;
        }
    }
}
