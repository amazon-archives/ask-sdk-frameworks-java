package com.amazon.ask.tictactoe.slots;

import com.amazon.ask.models.annotation.data.SlotTypeResource;
import com.amazon.ask.models.annotation.type.SlotType;

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

