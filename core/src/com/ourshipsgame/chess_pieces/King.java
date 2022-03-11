package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;

public class King extends Chess{
    private boolean check;
    private boolean mate;

    public King(Texture chessTexture, float posX, float posY) {
        super(chessTexture, posX, posY);
    }

    @Override
    protected void moveChess(GameBoard.BoardLocations newPos) {

    }

    @Override
    protected void drawAvailableMovesAndAttacks() {

    }

    public boolean hasLost(){
        return check & mate;
    }
}
