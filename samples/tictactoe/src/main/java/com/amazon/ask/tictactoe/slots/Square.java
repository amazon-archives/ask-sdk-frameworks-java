/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.tictactoe.slots;

import com.amazon.ask.interaction.annotation.data.SlotTypeResource;
import com.amazon.ask.interaction.annotation.type.SlotType;

/**
 * 0 1 2
 * 3 4 5
 * 6 7 8
 */
@SlotType
@SlotTypeResource("square")
public enum Square {
    NORTH_WEST(0),
    NORTH(1),
    NORTH_EAST(2),
    WEST(3),
    CENTER(4),
    EAST(5),
    SOUTH_WEST(6),
    SOUTH(7),
    SOUTH_EAST(8);

    private final int index;

    Square(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

