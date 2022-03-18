package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ourshipsgame.game.GameBoard;

public class Queen extends Chess{
    public Queen(Texture chessTexture, GameBoard.BoardLocations location) {
        super(chessTexture, location);
    }

    @Override
    protected void moveChess(GameBoard.BoardLocations newPos) {

    }

    @Override
    protected void drawAvailableMovesAndAttacks(SpriteBatch spriteBatch) {

    }

    @Override
    protected boolean canMove(GameBoard.BoardLocations position) {
        return false;
    }
}
