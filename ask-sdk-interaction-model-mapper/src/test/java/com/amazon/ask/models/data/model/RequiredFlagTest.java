package com.amazon.ask.models.data.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class RequiredFlagTest {

    @Test
    public void testBothNull() {
        assertNull(RequiredFlag.choose(null, null));
    }

    @Test
    public void testParentNull() {
        assertNull(RequiredFlag.choose(null, null));
        assertFalse(RequiredFlag.choose(null, false));
        assertTrue(RequiredFlag.choose(null, true));
    }

    @Test
    public void testTrue() {
        assertTrue(RequiredFlag.choose(true, null));
        assertTrue(RequiredFlag.choose(true, false));
        assertTrue(RequiredFlag.choose(true, true));
    }

    @Test
    public void testParentFalse() {
        assertFalse(RequiredFlag.choose(false, null));
        assertFalse(RequiredFlag.choose(false, false));
        assertTrue(RequiredFlag.choose(false, true));
    }
}
