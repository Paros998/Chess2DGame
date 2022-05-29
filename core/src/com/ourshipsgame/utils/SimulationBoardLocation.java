package com.ourshipsgame.utils;

import com.ourshipsgame.game.GameBoard;

public class SimulationBoardLocation {
    private final Vector2i arrayPosition;
    private SimulationChess chess;

    public SimulationBoardLocation(Vector2i arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    public Vector2i getArrayPosition() {
        return arrayPosition;
    }

    public SimulationChess getChess() {
        return chess;
    }

    public SimulationBoardLocation setChess(SimulationChess chess, boolean init) {
        this.chess = chess;
        this.chess.setCurrentLocation(this);
        return this;
    }

    public SimulationBoardLocation setChess(SimulationChess chess) {

        if (chess != null)
            if (this.chess != null) {
                this.chess.setDestroyed(true);
                this.chess.setCurrentLocation(null);
            }

        this.chess = chess;
        this.chess.setCurrentLocation(this);
        return this;
    }

}
