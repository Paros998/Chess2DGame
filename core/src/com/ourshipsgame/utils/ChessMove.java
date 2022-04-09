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
        String[] stringMove = line.split("[A-H]+[1-8]",2);

        return new ChessMove(
               GameBoard.BoardLocations.valueOf(stringMove[0]),
               GameBoard.BoardLocations.valueOf(stringMove[1])
        );
    }

}
