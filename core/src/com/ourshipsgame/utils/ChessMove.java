package com.ourshipsgame.utils;

import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.Player;

public class ChessMove {
    final GameBoard.BoardLocations pieceLocation;
    final GameBoard.BoardLocations moveDestination;

    public ChessMove(GameBoard.BoardLocations pieceLocation, GameBoard.BoardLocations moveDestination) {
        this.pieceLocation = pieceLocation;
        this.moveDestination = moveDestination;
    }

    public GameBoard.BoardLocations getPieceLocation() {
        return pieceLocation;
    }


    public GameBoard.BoardLocations getMoveDestination() {
        return moveDestination;
    }

    public String write() {
        return  pieceLocation.name() + " " + getMoveDestination().name();
    }
}
