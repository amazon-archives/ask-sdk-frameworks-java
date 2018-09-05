/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.data.model;

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
