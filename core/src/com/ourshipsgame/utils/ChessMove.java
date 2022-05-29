package com.ourshipsgame.utils;

import com.ourshipsgame.game.GameBoard;

public class ChessMove {
    final GameBoard.BoardLocations moveLocation;
    final GameBoard.BoardLocations moveDestination;
    final typesOfMoves moveType;
    final pieceType piece;


    public enum typesOfMoves {
        NORMAL, CHANGEFIGURE
    }

    public enum pieceType {
        B_BISHOP, B_KNIGHT, B_QUEEN, B_ROOK, B_NOCHANGE
    }

    public ChessMove(GameBoard.BoardLocations pieceLocation, GameBoard.BoardLocations moveDestination, typesOfMoves type, pieceType piece) {
        this.moveLocation = pieceLocation;
        this.moveDestination = moveDestination;
        this.moveType = type;
        this.piece = piece;
    }

    public GameBoard.BoardLocations getMoveLocation() {
        return moveLocation;
    }


    public GameBoard.BoardLocations getMoveDestination() {
        return moveDestination;
    }

    public typesOfMoves getMoveType() {
        return moveType;
    }

    public pieceType getDesiredFigure() {
        return piece;
    }

    public String write() {
        return moveLocation.name() + "." + getMoveDestination().name() + "." + moveType.name() + "." + this.piece.name();
    }

    public static ChessMove readFromLine(String line) {
        String[] stringMove = line.split("[.]", 0);

        return new ChessMove(
                GameBoard.BoardLocations.valueOf(stringMove[0].trim()),
                GameBoard.BoardLocations.valueOf(stringMove[1].trim()),
                typesOfMoves.valueOf(stringMove[2].trim()),
                pieceType.valueOf(stringMove[3].trim())
        );
    }

}
