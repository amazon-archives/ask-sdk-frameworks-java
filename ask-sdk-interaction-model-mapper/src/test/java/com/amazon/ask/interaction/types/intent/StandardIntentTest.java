/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.types.intent;

import com.amazon.ask.interaction.Utils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *
 */
public class StandardIntentTest {
    private static final List<Class<? extends StandardIntent>> BUILT_INS = new ArrayList<>();
    static {
        BUILT_INS.add(CancelIntent.class);
        BUILT_INS.add(FallbackIntent.class);
        BUILT_INS.add(HelpIntent.class);
        BUILT_INS.add(LoopOffIntent.class);
        BUILT_INS.add(LoopOnIntent.class);
        BUILT_INS.add(MoreIntent.class);
        BUILT_INS.add(NavigateHomeIntent.class);
        BUILT_INS.add(NavigateSettingsIntent.class);
        BUILT_INS.add(NextIntent.class);
        BUILT_INS.add(NoIntent.class);
        BUILT_INS.add(PageDownIntent.class);
        BUILT_INS.add(PageUpIntent.class);
        BUILT_INS.add(PauseIntent.class);
        BUILT_INS.add(PreviousIntent.class);
        BUILT_INS.add(RepeatIntent.class);
        BUILT_INS.add(ResumeIntent.class);
        BUILT_INS.add(ScrollDownIntent.class);
        BUILT_INS.add(ScrollLeftIntent.class);
        BUILT_INS.add(ScrollRightIntent.class);
        BUILT_INS.add(ScrollUpIntent.class);
        BUILT_INS.add(ShuffleOffIntent.class);
        BUILT_INS.add(ShuffleOnIntent.class);
        BUILT_INS.add(StartOverIntent.class);
        BUILT_INS.add(StopIntent.class);
        BUILT_INS.add(YesIntent.class);
    }

    @Test
    public void testEquals() {
        for (Class<? extends StandardIntent> intentClass : BUILT_INS) {
            StandardIntent intent1 = Utils.instantiate(intentClass);
            StandardIntent intent2 = Utils.instantiate(intentClass);

            assertEqualsIntent(intent1, intent1); // self
            assertEqualsIntent(intent1, intent2);
            assertNotEquals(null, intent1);
            assertNotEquals(intent1, null);
            assertNotEquals(intent1, "differentClass");
            assertNotEquals("differentClass", intent1);
        }
    }

    private static void assertEqualsIntent(StandardIntent left, StandardIntent right) {
        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertEquals(left.toString(), right.toString());
    }
}
