package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;

public class King extends Chess{
    private boolean check;
    private boolean mate;

    public King(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
    }

    @Override
    protected void calculatePossibleMoves(GameBoard gameBoard) {

    }

    public boolean hasLost(){
        return check & mate;
    }
}
