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

import com.amazon.ask.interaction.model.InteractionModelEnvelope;
import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.interaction.Locales;
import com.amazon.ask.interaction.annotation.data.SlotTypeSkillResource;
import com.amazon.ask.interaction.data.model.SlotTypeData;
import com.amazon.ask.interaction.data.source.Codec;
import com.amazon.ask.interaction.data.source.JsonCodec;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.mapper.slot.SlotPropertyReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SlotTypeSkillResourceTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Codec<InteractionModelEnvelope> CODEC = new JsonCodec<>(MAPPER.readerFor(InteractionModelEnvelope.class));

    private final SlotTypeSkillResource.Plugin underTest = new SlotTypeSkillResource.Plugin();

    @Mock
    private SlotPropertyReader mockSlotParser;

    SlotTypeSkillResource defaultSlotTypeSkillResource = DefaultModel.class.getAnnotation(SlotTypeSkillResource.class);
    SlotTypeSkillResource emptySlotTypeSkillResource = EmptyModel.class.getAnnotation(SlotTypeSkillResource.class);

    private RenderContext<SlotTypeDefinition> makeInput(String slotType, Class<?> slotClass) {
        return RenderContext.slotType()
            .withLocale(Locales.en_US)
            .withValue(SlotTypeDefinition.builder()
                .withCustom(false)
                .withName(slotType)
                .withSlotTypeClass(slotClass)
                .build())
            .build();
    }

    @SlotTypeSkillResource("interaction_model_test")
    public static class DefaultModel {
    }

    @SlotTypeSkillResource("interaction_model_test_empty")
    public static class EmptyModel {
    }

    @Test
    public void testMissing() {
        SlotTypeData actual = underTest.apply(makeInput("missing", DefaultModel.class), defaultSlotTypeSkillResource).reduce(SlotTypeData::combine).get();
        assertEquals(SlotTypeData.builder().build(), actual);
    }

    @Test
    public void testNoSynonyms() {
        SlotTypeData expected = SlotTypeData.builder()
            .addValue("A", SlotValue.builder()
                .withValue("a")
                .build())
            .build();

        SlotTypeData actual = underTest.apply(makeInput("test_no_synonyms", DefaultModel.class), defaultSlotTypeSkillResource).reduce(SlotTypeData::combine).get();

        assertEquals(expected, actual);
    }

    @Test
    public void testSynonyms() {
        SlotTypeData expected = SlotTypeData.builder()
            .addValue("A", SlotValue.builder()
                .withValue("a")
                .withSynonyms(Collections.singletonList("b"))
                .build())
            .build();
        SlotTypeData actual = underTest.apply(makeInput("test_synonyms", DefaultModel.class), defaultSlotTypeSkillResource).reduce(SlotTypeData::combine).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testNoTypes() {
        SlotTypeData expected = SlotTypeData.builder().build();
        SlotTypeData actual = underTest.apply(makeInput("empty", EmptyModel.class), emptySlotTypeSkillResource).reduce(SlotTypeData::combine).get();
        assertEquals(expected, actual);
    }
}
