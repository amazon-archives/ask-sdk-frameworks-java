/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.mapper.slot.RawSlotPropertyReader;

public abstract class BaseSlotValue {
    @SlotPropertyReader(RawSlotPropertyReader.class)
    private Slot slot;

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseSlotValue that = (BaseSlotValue) o;

        return slot.equals(that.slot);
    }

    @Override
    public int hashCode() {
        return slot.hashCode();
    }
}
