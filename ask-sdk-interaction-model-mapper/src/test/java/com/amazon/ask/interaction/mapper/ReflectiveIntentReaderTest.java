package com.amazon.ask.interaction.mapper;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.TypeReflector;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.mapper.intent.IntentSlotPropertyReader;
import com.amazon.ask.interaction.mapper.intent.ReflectiveIntentReader;
import com.amazon.ask.interaction.mapper.slot.AmazonNumberParser;
import com.amazon.ask.interaction.types.slot.AmazonNumber;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectiveIntentReaderTest {
    private final ReflectiveIntentReader<TestIntent> underTest = new ReflectiveIntentReader<>(
        new TypeReflector<>(TestIntent.class),
        Collections.singletonMap("slot", new IntentSlotPropertyReader<>(new AmazonNumberParser(), "slot")));

    @Test
    public void testParse() throws IntentParseException {
       Slot slot = Slot.builder()
            .withName("slot")
            .withValue("1")
            .build();
       IntentRequest request = IntentRequest.builder()
            .withRequestId("id")
            .withIntent(com.amazon.ask.model.Intent.builder()
                .withName("test")
                .withSlots(Collections.singletonMap("slot", slot))
                .build())
            .build();

        assertEquals(new AmazonNumber(slot, 1), underTest.read(request).getSlot());
    }

    @Test(expected = UnrecognizedSlotException.class)
    public void testMissingSlot() throws IntentParseException {
        IntentRequest request = IntentRequest.builder()
            .withIntent(com.amazon.ask.model.Intent.builder()
                .withName("TestIntent")
                .withSlots(Collections.emptyMap())
                .build())
            .build();

        assertNull(underTest.read(request).getSlot());
    }

    @Test
    public void testNullSlot() throws IntentParseException {
        IntentRequest request = IntentRequest.builder()
            .withIntent(com.amazon.ask.model.Intent.builder()
                .withName("TestIntent")
                .withSlots(Collections.singletonMap("slot", null))
                .build())
            .build();

        assertNull(underTest.read(request).getSlot());
    }

    @Test
    public void testNullSlotValue() throws IntentParseException {
        IntentRequest request = IntentRequest.builder()
            .withIntent(com.amazon.ask.model.Intent.builder()
                .withName("TestIntent")
                .withSlots(Collections.singletonMap("slot", Slot.builder()
                    .withName("slot")
                    .withValue(null)
                    .build()))
                .build())
            .build();

        assertNull(underTest.read(request).getSlot());
    }

    @Test(expected = IntentParseException.class)
    public void testSlotParseException() throws IntentParseException {
        IntentRequest request = IntentRequest.builder()
            .withRequestId("id")
            .withIntent(com.amazon.ask.model.Intent.builder()
                .withName("TestIntent")
                .withSlots(Collections.singletonMap("slot", Slot.builder()
                    .withName("slot")
                    .withValue("invalid")
                    .build()))
                .build())
            .build();

        underTest.read(request);
    }

//    @Test(expected = IllegalStateException.class)
//    public void testNoSetter() throws IntentParseException {
//        testErrorCase(TestIntentNoSetter.class);
//    }

//    @Test(expected = IllegalStateException.class)
//    public void testIllegalAccess() throws IntentParseException {
//        testErrorCase(TestIntentIllegalAccess.class);
//    }

    @Test(expected = IllegalStateException.class)
    public void testInstantiationError() throws IntentParseException {
        testErrorCase(TestIntentInstantiationError.class);
    }

    private <T> void testErrorCase(Class<T> clazz) throws IntentParseException {
        IntentRequest request = IntentRequest.builder()
            .withRequestId("id")
            .withIntent(com.amazon.ask.model.Intent.builder()
                .withName("TestIntent")
                .withSlots(Collections.singletonMap("slot", Slot.builder()
                    .withName("slot")
                    .withValue("1")
                    .build()))
                .build())
            .build();

        IntentMapper.fromModel(Model.empty()).intentReaderFor(clazz).read(request);
    }

    @Intent(value = "TestIntent")
    public static final class TestIntent { // valid bean
        @SlotProperty
        private AmazonNumber slot;
        public AmazonNumber getSlot() { return slot;}
        public void setSlot(AmazonNumber slot) { this.slot = slot; }
    }

    @Intent(value = "TestIntentNoSetter")
    public static final class TestIntentNoSetter { // setSlot is missing
        @SlotProperty
        private AmazonNumber slot;
        public AmazonNumber getSlot() { return slot; }
    }

    @Intent(value = "TestIntentIllegalAccess")
    public static final class TestIntentIllegalAccess { // illegal access of private setter
        @SlotProperty
        private AmazonNumber slot;
        private void setSlot(AmazonNumber slot) { this.slot = slot; }
    }

    @Intent(value = "TestIntentInstantiationError")
    public static final class TestIntentInstantiationError { // missing an unit constructor
        public TestIntentInstantiationError(String dummy) { }
        @SlotProperty
        private Integer slot;
        public Integer getSlot() { return slot;}
        public void setSlot(Integer slot) { this.slot = slot; }
    }
}
