package com.amazon.ask.interaction.data.model;

import com.amazon.ask.interaction.model.DialogIntentPrompt;
import com.amazon.ask.interaction.model.PromptVariation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class IntentDataTest {

    @Test
    public void testNotEqualsNull() {
        IntentData test = mockBuilder("test").build();

        assertNotEquals(test, null);
        assertNotEquals(null, test);
    }

    @Test
    public void testEqualsSelf() {
        IntentData left = mockBuilder("test").build();

        assertEquals(left, left);
        assertEquals(left.hashCode(), left.hashCode());
    }

    @Test
    public void testEquals() {
        IntentData left = mockBuilder("test").build();
        IntentData right = mockBuilder("test").build();

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEquals() {
        IntentData left = mockBuilder("left").build();
        IntentData right = mockBuilder("right").build();


        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentConfirmationRequired() {
        IntentData left = mockBuilder("test").withConfirmationRequired(true).build();
        IntentData right = mockBuilder("test").build();


        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentConfirmation() {
        IntentData left = mockBuilder("test").withConfirmation("different").build();
        IntentData right = mockBuilder("test").build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentElicitationRequired() {
        IntentData left = mockBuilder("test").withElicitationRequired(true).build();
        IntentData right = mockBuilder("test").build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentElicitation() {
        IntentData left = mockBuilder("test").withElicitation("different").build();
        IntentData right = mockBuilder("test").build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentSamples() {
        IntentData left = mockBuilder("test").withSample("different").build();
        IntentData right = mockBuilder("test").build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentSlotSample() {
        IntentData left = mockBuilder("test").withSlotSample("different").build();

        assertNotEquals(left, "wrong class");
    }

    @Test
    public void testNotEqualsWrongClass() {
        IntentData left = mockBuilder("test").build();

        assertNotEquals(left, "wrong class");
    }

    // TODO: relocate
//    @Test
//    public void testMerge() {
//        IntentData left = IntentData.builder().build();
//        IntentData right = mockBuilder("test").build();
//
//        assertEquals(Optional.of(right), IntentData.reducer().apply(Stream.of(left, right)));
//    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfirmationPromptConflict() {
        IntentData.builder()
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("test")
                .build())
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("different")
                .build());
    }

    @Test
    public void testConfirmationPromptNoConflict() {
        IntentData.builder()
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("no conflict")
                .build())
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("no conflict")
                .build());
    }

    @Test
    public void testNullConfirmationPrompt() {
        IntentData.builder()
            .withPrompts(DialogIntentPrompt.builder()
                .withConfirmation("no conflict")
                .build())
            .withPrompts(null);
    }

    // Helper for constructing instances that slightly vary
    private static Builder mockBuilder(String defaultValue) {
        return new Builder(defaultValue);
    }
    private static class Builder {
        private final String defaultValue;

        private String sample;
        private String slot;
        private Boolean confirmationRequired;
        private String confirmation;
        private Boolean elicitationRequired;
        private String elicitation;
        private String slotSample;

        private Builder(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Builder withSample(String sample) {
            this.sample = sample;
            return this;
        }

        public Builder withSlot(String slot) {
            this.slot = slot;
            return this;
        }

        public Builder withConfirmation(String confirmation) {
            this.confirmation = confirmation;
            return this;
        }

        public Builder withConfirmationRequired(Boolean confirmationRequired) {
            this.confirmationRequired = confirmationRequired;
            return this;
        }

        public Builder withElicitation(String elicitation) {
            this.elicitation = elicitation;
            return this;
        }

        public Builder withElicitationRequired(Boolean elicitationRequired) {
            this.elicitationRequired = elicitationRequired;
            return this;
        }

        public Builder withSlotSample(String slotSample) {
            this.slotSample = slotSample;
            return this;
        }

        public IntentData build() {
            return IntentData.builder()
                .addSample(resolve(sample))
                .addSlot(resolve(slot), IntentSlotData.builder()
                    .withConfirmationRequired(confirmationRequired)
                    .addConfirmation(PromptVariation.builder()
                        .withType("PlainText")
                        .withValue(resolve(confirmation))
                        .build())
                    .withElicitationRequired(elicitationRequired)
                    .addElicitation(PromptVariation.builder()
                        .withType("PlainText")
                        .withValue(resolve(elicitation))
                        .build())
                    .addSample(resolve(slotSample))
                    .build())
                .withPrompts(DialogIntentPrompt.builder()
                    .withConfirmation(resolve(confirmation))
                    .build())
                .build();
        }

        private String resolve(String setValue) {
            return setValue == null ? defaultValue : setValue;
        }
    }
}
