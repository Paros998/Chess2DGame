package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ourshipsgame.game.GameBoard;

public class Queen extends Chess{
    public Queen(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
    }

    @Override
    protected void calculatePossibleMoves(GameBoard gameBoard) {

    }


}
