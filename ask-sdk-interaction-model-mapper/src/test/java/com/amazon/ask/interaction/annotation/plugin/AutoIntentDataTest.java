package com.amazon.ask.interaction.annotation.plugin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class AutoIntentDataTest {

    @Test
    public void instancesShouldBeEqual() {
        assertEquals(new AutoIntentData.Scanner(), new AutoIntentData.Scanner());
        assertEquals(new AutoIntentData.Scanner().hashCode(), new AutoIntentData.Scanner().hashCode());
    }
}
