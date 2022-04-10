package com.ourshipsgame.utils;

import com.ourshipsgame.game.GameBoard;

public class ChessMove {
    final GameBoard.BoardLocations moveLocation;
    final GameBoard.BoardLocations moveDestination;

    public ChessMove(GameBoard.BoardLocations pieceLocation, GameBoard.BoardLocations moveDestination) {
        this.moveLocation = pieceLocation;
        this.moveDestination = moveDestination;
    }

    public GameBoard.BoardLocations getMoveLocation() {
        return moveLocation;
    }


    public GameBoard.BoardLocations getMoveDestination() {
        return moveDestination;
    }

    public String write() {
        return  moveLocation.name() + " " + getMoveDestination().name();
    }

    public static ChessMove readFromLine(String line){
        String[] stringMove = new String[2];
        stringMove[0] = line.substring(0, 2);
        stringMove[1] = line.substring(3, 5);

        return new ChessMove(
               GameBoard.BoardLocations.valueOf(stringMove[0].trim()),
               GameBoard.BoardLocations.valueOf(stringMove[1].trim())
        );
    }

}
