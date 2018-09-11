/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.types.slot.date;

import com.amazon.ask.model.Slot;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * TODO: Can't find docs for this anymore?
 */
public class PresentRef extends AmazonDate {
    private final LocalDateTime time;

    public PresentRef(Slot slot, LocalDateTime time) {
        super(slot);
        this.time = assertNotNull(time, "time");
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        PresentRef that = (PresentRef) o;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), time);
    }

    @Override
    public String toString() {
        return "PresentRef{" + "time=" + time + ", slot=" + getSlot() + '}';
    }
}
