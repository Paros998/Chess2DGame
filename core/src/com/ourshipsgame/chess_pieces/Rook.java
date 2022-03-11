package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;

public class Rook extends Chess{
    public Rook(Texture chessTexture, float posX, float posY) {
        super(chessTexture, posX, posY);
    }

    @Override
    protected void moveChess(GameBoard.BoardLocations newPos) {

    }

    @Override
    protected void drawAvailableMovesAndAttacks() {

    }
}
