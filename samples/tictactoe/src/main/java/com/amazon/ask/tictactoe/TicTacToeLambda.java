package com.amazon.ask.tictactoe;

import com.amazon.ask.SkillStreamHandler;

/**
 *
 */
public class TicTacToeLambda extends SkillStreamHandler {
    public TicTacToeLambda() {
        super(new TicTacToe().getSkill());
    }
}
