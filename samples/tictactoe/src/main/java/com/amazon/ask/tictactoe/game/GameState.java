package com.amazon.ask.tictactoe.game;

import com.amazon.ask.tictactoe.interaction.PlayMove;
import com.amazon.ask.tictactoe.interaction.Square;

import java.util.*;

/**
 *
 */
public class GameState {
    private static final List<Square[]> WINING_COMBINATIONS = Arrays.asList(
        // horizontal
        new Square[]{Square.NORTH_WEST, Square.NORTH, Square.NORTH_EAST},
        new Square[]{Square.WEST, Square.CENTER, Square.EAST},
        new Square[]{Square.SOUTH_WEST, Square.SOUTH, Square.SOUTH_EAST},

        // vertical
        new Square[]{Square.NORTH_WEST, Square.WEST, Square.SOUTH_WEST},
        new Square[]{Square.NORTH, Square.CENTER, Square.SOUTH},
        new Square[]{Square.NORTH_EAST, Square.EAST, Square.SOUTH_EAST},

        // X's
        new Square[]{Square.NORTH_WEST, Square.CENTER, Square.SOUTH_EAST},
        new Square[]{Square.NORTH_EAST, Square.CENTER, Square.NORTH_EAST}
    ) ;

    private String currentPlayer;
    private Map<String, String> board;

    public Player getCurrentPlayer() {
        return Player.valueOf(currentPlayer);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer.name();
    }

    public Map<String, String> getBoard() {
        return board;
    }

    public void setBoard(Map<String, String> board) {
        this.board = board;
    }

    public MoveResult playMove(PlayMove move, Player player) {
        if(this.board == null) {
            this.board = new HashMap<>();
        }
        if (this.board.containsKey(move.getSquare().name())) {
            return MoveResult.ILLEGAL;
        } else {
            this.board.put(move.getSquare().name(), player.name());
            return getResult(player);
        }
    }

    private MoveResult getResult(Player player) {
        if (this.board == null) {
            return MoveResult.LEGAL;
        }
        for (Square[] combination : WINING_COMBINATIONS) {
            boolean win = true;
            for (Square square : combination) {
                Optional<Player> s = getSquare(square);
                win = s.isPresent() && win && s.get().equals(player);
            }
            if (win) {
                return MoveResult.WIN;
            }
        }
        if (board.size() == 9) {
            return MoveResult.DRAW;
        } else {
            return MoveResult.LEGAL;
        }
    }

    private Optional<Player> getSquare(Square square) {
        return Optional.ofNullable(board).map(b -> b.get(square.name())).map(Player::valueOf);
    }
}
