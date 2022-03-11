package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;

public class Bishop extends Chess{
    public Bishop(Texture chessTexture, float posX, float posY) {
        super(chessTexture, posX, posY);
    }

    @Override
    protected void moveChess(GameBoard.BoardLocations newPos) {

    }

    @Override
    protected void drawAvailableMovesAndAttacks() {

    }
}
