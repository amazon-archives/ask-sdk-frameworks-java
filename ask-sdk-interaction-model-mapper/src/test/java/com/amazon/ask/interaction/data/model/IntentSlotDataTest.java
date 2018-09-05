/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.data.model;

import com.amazon.ask.interaction.model.DialogSlotPrompt;
import com.amazon.ask.interaction.model.PromptVariation;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class IntentSlotDataTest {

    @Test
    public void testEqualsSelf() {
        IntentSlotData test = mockBuilder("test").build();

        assertEquals(test, test);
        assertEquals(test.hashCode(), test.hashCode());
    }

    @Test
    public void testNotEqualsNull() {
        IntentSlotData test = mockBuilder("test").build();

        assertNotEquals(test, null);
        assertNotEquals(null, test);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        IntentSlotData test = mockBuilder("test").build();

        assertNotEquals(test, "different");
        assertNotEquals("different", test);
    }

    @Test
    public void testNotEqualsDifferentConfirmations() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("left")
            .withConfirmation("different")
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentConfirmationRequired() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("left")
            .withConfirmationRequired(false)
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }


    @Test
    public void testNotEqualsDifferentElicitations() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("left")
            .withElicitation("different")
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentElicitationRequired() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("left")
            .withElicitationRequired(false)
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentConfirmationPrompt() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("left")
            .withConfirmationPrompt("different")
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentElicitationPrompt() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("left")
            .withElicitationPrompt("different")
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualsDifferentSamples() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("left")
            .withSlotSample("different")
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testMerge() {
        IntentSlotData left = IntentSlotData.builder().build();
        IntentSlotData right = mockBuilder("right").build();
        IntentSlotData merged = IntentSlotData.builder().merge(left).merge(right).build();

        assertEquals(right, merged);
        assertEquals(right.hashCode(), merged.hashCode());
    }

    @Test
    public void testMergeSelf() {
        IntentSlotData test = mockBuilder("test").build();
        assertEquals(test, IntentSlotData.builder().merge(test).merge(test).build());
    }

    @Test
    public void testMergeMissingPrompt() {
        IntentSlotData left = IntentSlotData.builder()
            .withPrompts(null)
            .build();
        IntentSlotData right = mockBuilder("right").build();
        IntentSlotData mergedLeft = IntentSlotData.builder().merge(left).merge(right).build();
        IntentSlotData mergedRight = IntentSlotData.builder().merge(right).merge(left).build();

        assertEquals(right, mergedLeft);
        assertEquals(right.hashCode(), mergedLeft.hashCode());
        assertEquals(right, mergedRight);
        assertEquals(right.hashCode(), mergedRight.hashCode());
        assertEquals(mergedLeft, mergedRight);
        assertEquals(mergedLeft.hashCode(), mergedRight.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergePromptsConflict() {
        IntentSlotData left = mockBuilder("left").build();
        IntentSlotData right = mockBuilder("right").build();
        IntentSlotData.builder().merge(left).merge(right).build();
    }

    // Helper for constructing instances that slightly vary
    private static Builder mockBuilder(String defaultValue) {
        return new Builder(defaultValue);
    }
    private static class Builder {
        private final String defaultValue;

        private Boolean confirmationRequired;
        private String confirmation;
        private String confirmationPrompt;
        private Boolean elicitationRequired;
        private String elicitation;
        private String elicitationPrompt;
        private String slotSample;

        private Builder(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Builder withConfirmation(String confirmation) {
            this.confirmation = confirmation;
            return this;
        }

        public Builder withConfirmationPrompt(String confirmationPrompt) {
            this.confirmationPrompt = confirmationPrompt;
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

        public Builder withElicitationPrompt(String elicitationPrompt) {
            this.elicitationPrompt = elicitationPrompt;
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

        public IntentSlotData build() {
            return IntentSlotData.builder()
                .withConfirmationRequired(confirmationRequired)
                .withPrompts(DialogSlotPrompt.builder()
                    .withConfirmation(resolve(confirmationPrompt))
                    .withElicitation(resolve(elicitationPrompt))
                    .build())
                .addConfirmation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue(resolve(confirmation))
                    .build())
                .withElicitationRequired(elicitationRequired)
                .addElicitation(PromptVariation.builder()
                    .withType("PlainText")
                    .withValue(resolve(elicitation))
                    .build())
                .addSamples(Collections.singletonList(
                    resolve(slotSample)
                ))
                .build();
        }

        private String resolve(String setValue) {
            return setValue == null ? defaultValue : setValue;
        }
    }
}
