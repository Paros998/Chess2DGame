package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;


public class Pawn extends Chess{
    private boolean firstMove;

    public Pawn(Texture chessTexture, float posX, float posY) {
        super(chessTexture, posX, posY);
        firstMove = true;
    }

    @Override
    protected void moveChess(GameBoard.BoardLocations newPos) {

    }

    @Override
    protected void drawAvailableMovesAndAttacks() {

    }
}
