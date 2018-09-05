/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.renderer.models;

import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.type.Intent;

/**
 *
 */
@Intent("TestPrompts")
@IntentResource("TestPrompts")
public class TestPrompts {
    @SlotProperty
    private TestCustom customSlot;

    public TestCustom getCustomSlot() {
        return customSlot;
    }

    public void setCustomSlot(TestCustom customSlot) {
        this.customSlot = customSlot;
    }
}
