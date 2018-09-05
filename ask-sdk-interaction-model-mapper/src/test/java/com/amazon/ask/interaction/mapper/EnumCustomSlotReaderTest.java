/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.mapper;

import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.slu.entityresolution.Resolutions;
import com.amazon.ask.model.slu.entityresolution.Status;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.amazon.ask.model.slu.entityresolution.ValueWrapper;
import com.amazon.ask.model.slu.entityresolution.Value;
import com.amazon.ask.interaction.mapper.slot.EnumCustomSlotReader;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class EnumCustomSlotReaderTest {
    private enum TestEnum { A, B }

    private final EnumCustomSlotReader<TestEnum> underTest = new EnumCustomSlotReader<>(TestEnum.class);

    @Test
    public void testParseValue() throws IntentParseException {
        assertEquals(TestEnum.A, underTest.read(null, slot("A")));
        assertEquals(TestEnum.B, underTest.read(null, slot("B")));
    }

    @Test(expected = SlotValueParseException.class)
    public void testParseInvalidSymbol() throws IntentParseException {
        underTest.read(null, slot("unknown"));
    }

    private static Slot slot(String value) {
        return Slot.builder()
            .withResolutions(Resolutions.builder()
                .withResolutionsPerAuthority(Collections.emptyList())
                .build())
            .withName("test")
            .withValue(value)
            .build();
    }

    @Test
    public void testParseMatchedResolutionId() throws SlotValueParseException {
        Slot slot = Slot.builder()
            .withValue("spoken")
            .withResolutions(Resolutions.builder()
                .withResolutionsPerAuthority(Arrays.asList(
                    resolution("A", StatusCode.ER_SUCCESS_MATCH)
                ))
                .build())
            .build();

        assertEquals(TestEnum.A, underTest.read(null, slot));
    }

    @Test
    public void testParseFirstMatchedResolutionId() throws SlotValueParseException {
        Slot slot = Slot.builder()
            .withValue("spoken value")
            .withResolutions(Resolutions.builder()
                .withResolutionsPerAuthority(Arrays.asList(
                    resolution("B", StatusCode.ER_SUCCESS_NO_MATCH),
                    resolution("A", StatusCode.ER_SUCCESS_MATCH)
                ))
                .build())
            .build();

        assertEquals(TestEnum.A, underTest.read(null, slot));
    }

    @Test
    public void testParseValueIfNoMatch() throws SlotValueParseException {
        Slot slot = Slot.builder()
            .withValue("A")
            .withResolutions(Resolutions.builder()
                .withResolutionsPerAuthority(Arrays.asList(
                    resolution("B", StatusCode.ER_SUCCESS_NO_MATCH)
                ))
                .build())
            .build();

        assertEquals(TestEnum.A, underTest.read(null, slot));
    }

    @Test(expected = SlotValueParseException.class)
    public void testFailIfNoMatchAndInvalidSymbol() throws SlotValueParseException {
        Slot slot = Slot.builder()
            .withValue("invalid symbol")
            .withResolutions(Resolutions.builder()
                .withResolutionsPerAuthority(Arrays.asList(
                    resolution("B", StatusCode.ER_SUCCESS_NO_MATCH)
                ))
                .build())
            .build();

        assertEquals(TestEnum.A, underTest.read(null, slot));
    }

    private static Resolution resolution(String id, StatusCode code) {
        return Resolution.builder()
            .withValues(Collections.singletonList(
                ValueWrapper.builder()
                    .withValue(Value.builder()
                        .withId(id)
                        .build())
                    .build()
            ))
            .withStatus(Status.builder()
                .withCode(code)
                .build())
            .build();
    }
}
