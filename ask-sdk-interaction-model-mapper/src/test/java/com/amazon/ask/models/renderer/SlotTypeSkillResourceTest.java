package com.amazon.ask.models.renderer;

import com.amazon.ask.interaction.model.SkillModel;
import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.models.Locales;
import com.amazon.ask.models.annotation.data.SlotTypeSkillResource;
import com.amazon.ask.models.data.model.SlotTypeData;
import com.amazon.ask.models.data.source.Codec;
import com.amazon.ask.models.data.source.JsonCodec;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.amazon.ask.models.mapper.slot.SlotPropertyReader;
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
    private static final Codec<SkillModel> CODEC = new JsonCodec<>(MAPPER.readerFor(SkillModel.class));

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
