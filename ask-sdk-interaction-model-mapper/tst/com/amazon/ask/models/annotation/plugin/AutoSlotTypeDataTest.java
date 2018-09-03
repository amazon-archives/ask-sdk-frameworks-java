package com.amazon.ask.models.annotation.plugin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class AutoSlotTypeDataTest {
    @Test
    public void instancesShouldBeEqual() {
        assertEquals(new AutoSlotTypeData.Scanner(), new AutoSlotTypeData.Scanner());
        assertEquals(new AutoSlotTypeData.Scanner().hashCode(), new AutoSlotTypeData.Scanner().hashCode());
    }
}
