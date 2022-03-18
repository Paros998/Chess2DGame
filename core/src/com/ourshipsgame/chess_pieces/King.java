package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ourshipsgame.game.GameBoard;

public class King extends Chess{
    private boolean check;
    private boolean mate;

    public King(Texture chessTexture, GameBoard.BoardLocations location) {
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

    public boolean hasLost(){
        return check & mate;
    }
}
