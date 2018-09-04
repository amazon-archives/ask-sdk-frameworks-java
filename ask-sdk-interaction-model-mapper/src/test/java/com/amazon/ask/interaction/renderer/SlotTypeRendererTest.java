package com.amazon.ask.interaction.renderer;

import com.amazon.ask.interaction.definition.SlotTypeDefinition;
import com.amazon.ask.interaction.data.model.SlotTypeData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SlotTypeRendererTest {
    @Mock
    SlotTypeDefinition mockSlotType;

    @Mock
    SlotTypeData mockSlotData;

    SlotTypeRenderer underTest = new SlotTypeRenderer();

    @Before
    public void before() {
        when(mockSlotType.getName()).thenReturn("TypeName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlotType() {
        underTest.renderSlotType(null, mockSlotData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlotMetadata() {
        underTest.renderSlotType(null, mockSlotData);
    }

    @Test
    public void testNoValues() {
        when(mockSlotData.getValues()).thenReturn(Collections.emptyMap());

        assertEquals(
            underTest.renderSlotType(mockSlotType, mockSlotData),
            slotTypeBuilder()
                .withName("TypeName")
                .withValues(Collections.emptyList())
                .build());
    }

    @Test
    public void testRenderValue() {
        when(mockSlotData.getValues()).thenReturn(Collections.singletonMap(
            "test_id", slotValueBuilder()
                .withValue("test_value")
                .withSynonyms(Collections.singletonList("test_synonym"))
                .build()
        ));

        assertEquals(
            underTest.renderSlotType(mockSlotType, mockSlotData),
            slotTypeBuilder()
                .withName("TypeName")
                .withValues(Collections.singletonList(
                    slotTypeValueBuilder()
                        .withId("test_id")
                        .withName(slotValueBuilder()
                            .withValue("test_value")
                            .withSynonyms(Collections.singletonList("test_synonym"))
                            .build()
                        )
                        .build()
                ))
                .build());
    }

    private static com.amazon.ask.interaction.model.SlotType.Builder slotTypeBuilder() {
        return com.amazon.ask.interaction.model.SlotType.builder();
    }

    private static com.amazon.ask.interaction.model.SlotValue.Builder slotValueBuilder() {
        return com.amazon.ask.interaction.model.SlotValue.builder();
    }

    private static com.amazon.ask.interaction.model.SlotTypeValue.Builder slotTypeValueBuilder() {
        return com.amazon.ask.interaction.model.SlotTypeValue.builder();
    }
}
