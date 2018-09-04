package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.interaction.Locales;
import com.amazon.ask.interaction.data.model.SlotTypeData;
import com.amazon.ask.interaction.data.source.Codec;
import com.amazon.ask.interaction.data.source.JsonCodec;
import com.amazon.ask.interaction.data.source.ResourceSource;
import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ResourceSourceTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Codec<SlotTypeData> CODEC = new JsonCodec<>(MAPPER.readerFor(SlotTypeData.class));

    @Mock
    RenderContext<SlotTypeDefinition> mockInput;

    @Before
    public void before() {
        Mockito.when(mockInput.getLocale()).thenReturn(Locales.en_US);
    }

    @Test
    public void testLocalizedFolder() {
        ResourceSource<SlotTypeDefinition, SlotTypeData> resource = SlotTypeData.resource()
            .withName("folder")
            .withSuffix(".json")
            .withResourceClass(getClass())
            .build();

        assertEquals(
            resource.apply(mockInput),
            SlotTypeData.builder()
                .addValue("test", SlotValue.builder()
                    .withValue("en_US folder")
                    .withSynonyms(Collections.singletonList(
                        "en_US folder synonym"
                    ))
                    .build())
                .build());
    }

    @Test
    public void testGlobalFolder() {
        Mockito.when(mockInput.getLocale()).thenReturn(Locales.fr_FR);

        ResourceSource<SlotTypeDefinition, SlotTypeData> resource = SlotTypeData.resource()
            .withName("folder")
            .withSuffix(".json")
            .withResourceClass(getClass())
            .build();

        assertEquals(
            resource.apply(mockInput),
            SlotTypeData.builder()
                .addValue("test", SlotValue.builder()
                    .withValue("global folder")
                    .withSynonyms(Collections.singletonList(
                        "global folder synonym"
                    ))
                    .build())
                .build());
    }

    @Test
    public void testLocalizedFile() {
        ResourceSource<SlotTypeDefinition, SlotTypeData> resource = SlotTypeData.resource()
            .withName("file")
            .withSuffix(".json")
            .withResourceClass(getClass())
            .build();

        assertEquals(
            resource.apply(mockInput),
            SlotTypeData.builder()
                .addValue("test", SlotValue.builder()
                    .withValue("en_US file")
                    .withSynonyms(Collections.singletonList(
                        "en_US file synonym"
                    ))
                    .build())
                .build());
    }

    @Test
    public void testGlobalFile() {
        Mockito.when(mockInput.getLocale()).thenReturn(Locales.fr_FR);
        ResourceSource<SlotTypeDefinition, SlotTypeData> resource = SlotTypeData.resource()
            .withName("file")
            .withSuffix(".json")
            .withResourceClass(getClass())
            .build();

        assertEquals(
            resource.apply(mockInput),
            SlotTypeData.builder()
                .addValue("test", SlotValue.builder()
                    .withValue("global file")
                    .withSynonyms(Collections.singletonList(
                        "global file synonym"
                    ))
                    .build())
                .build());
    }

    @Test(expected = RuntimeException.class)
    public void testResourceNotFound() {
        SlotTypeData.resource()
            .withName("missing")
            .withSuffix(".json")
            .withResourceClass(getClass())
            .build()
            .apply(mockInput);
    }
}
