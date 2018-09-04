package com.amazon.ask.tictactoe.intents;

import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.tictactoe.slots.Square;

/**
 *
 */
@Intent
@IntentResource("play_move")
public class PlayMove {
    @SlotProperty
    private Square square;

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }
}
