package com.amazon.ask.models.data.model;

import com.amazon.ask.interaction.model.SlotTypeValue;
import com.amazon.ask.interaction.model.SlotValue;
import com.amazon.ask.models.definition.SlotTypeDefinition;
import com.amazon.ask.models.renderer.SlotTypeRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SlotDataTest {

    @Test
    public void testEqualsSelf() {
        SlotTypeData left = SlotTypeData.builder()
            .addValue("test", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();

        assertEquals(left, left);
        assertEquals(left.hashCode(), left.hashCode());
    }

    @Test
    public void testNotEqualsNull() {
        SlotTypeData left = SlotTypeData.builder()
            .addValue("test", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();

        assertNotEquals(left, null);
        assertNotEquals(null, left);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        SlotTypeData left = SlotTypeData.builder()
            .addValue("test", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();

        assertNotEquals(left, "test");
        assertNotEquals("test", left);
    }

    @Test
    public void testEquals() {
        SlotTypeData left = SlotTypeData.builder()
            .addValue("test", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();

        SlotTypeData right = SlotTypeData.builder()
            .addValue("test", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testDifferentValue() {
        SlotTypeData left = SlotTypeData.builder()
            .addValue("left", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();

        SlotTypeData right = SlotTypeData.builder()
            .addValue("right", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();

        assertNotEquals(left, right);
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedExistingValue() {
        SlotTypeData.builder()
            .addValue("test", SlotValue.builder()
                .withValue("test")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .addValue("test", SlotValue.builder()
                .withValue("different")
                .withSynonyms(Collections.singletonList("test"))
                .build())
            .build();
    }

    @Mock
    SlotTypeDefinition mockSlotType;

    @Test // TODO: Move to SlotTypeRendererImpl test
    public void testRenderSlotType() {
        SlotTypeData metadata = SlotTypeData.builder()
            .addValue("VALUE", SlotValue.builder()
                .withValue("value")
                .withSynonyms(Collections.singletonList("synonyms"))
                .build())
            .build();

        Mockito.when(mockSlotType.getName()).thenReturn("name");
        com.amazon.ask.interaction.model.SlotType expected = com.amazon.ask.interaction.model.SlotType.builder()
            .withName("name")
            .withValues(Collections.singletonList(SlotTypeValue.builder()
                .withId("VALUE")
                .withName(com.amazon.ask.interaction.model.SlotValue.builder()
                    .withValue("value")
                    .withSynonyms(Collections.singletonList("synonyms"))
                    .build())
                .build()))
            .build();

        SlotTypeRenderer renderer = new SlotTypeRenderer();

        assertEquals(expected, renderer.renderSlotType(mockSlotType, metadata));
    }

    // TODO: relocate these
//    @Test
//    public void testEmptyMerge() {
//        SlotTypeData first = SlotTypeData.builder().build();
//        SlotTypeData second = SlotTypeData.builder().build();
//
//        assertEquals(Optional.of(first), SlotTypeData.reducer().apply(Stream.of(first, second)));
//    }

//    @Test
//    public void testMerge() {
//        SlotTypeData first = SlotTypeData.builder()
//            .addValue("first", SlotValue.builder()
//                .withValue("first")
//                .withSynonyms(Collections.singletonList("first"))
//                .build())
//            .build();
//
//        SlotTypeData second = SlotTypeData.builder()
//            .addValue("second", SlotValue.builder()
//                .withValue("second")
//                .withSynonyms(Collections.singletonList("second"))
//                .build())
//            .build();
//
//        SlotTypeData expected = SlotTypeData.builder()
//            .addValue("first", SlotValue.builder()
//                .withValue("first")
//                .withSynonyms(Collections.singletonList("first"))
//                .build())
//            .addValue("second", SlotValue.builder()
//                .withValue("second")
//                .withSynonyms(Collections.singletonList("second"))
//                .build())
//            .build();
//
//        assertEquals(Optional.of(expected), SlotTypeData.reducer().apply(Stream.of(first, second)));
//    }
//
//    @Test
//    public void testMergeCollison() {
//        SlotTypeData first = SlotTypeData.builder()
//            .addValue("first", SlotValue.builder()
//                .withValue("first")
//                .withSynonyms(Collections.singletonList("first"))
//                .build())
//            .build();
//
//        SlotTypeData second = SlotTypeData.builder()
//            .addValue("first", SlotValue.builder()
//                .withValue("first")
//                .withSynonyms(Collections.singletonList("second"))
//                .build())
//            .build();
//
//        SlotTypeData expected = SlotTypeData.builder()
//            .addValue("first", SlotValue.builder()
//                .withValue("first")
//                .withSynonyms(Arrays.asList("first", "second"))
//                .build())
//            .build();
//
//        assertEquals(Optional.of(expected), SlotTypeData.reducer().apply(Stream.of(first, second)));
//    }
}
