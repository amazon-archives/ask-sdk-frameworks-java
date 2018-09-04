package com.amazon.ask.tictactoe.interaction;

import com.amazon.ask.models.annotation.data.IntentResource;
import com.amazon.ask.models.annotation.data.SlotProperty;
import com.amazon.ask.models.annotation.type.Intent;

/**
 *
 */
@Intent
@IntentResource("models/play_move")
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
