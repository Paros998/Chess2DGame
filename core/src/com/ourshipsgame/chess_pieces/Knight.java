package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.utils.Vector2i;

import java.util.ArrayList;


public class Knight extends Chess{
    public Knight(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
    }

    @Override
    protected void calculatePossibleMoves(GameBoard gameBoard) {
        Vector2i currentArrayPosition = currentLocation.getArrayPosition();

        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 2,currentArrayPosition.getY() + 1));
        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 2,currentArrayPosition.getY() - 1));
        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 2,currentArrayPosition.getY() + 1));
        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 2,currentArrayPosition.getY() - 1));

        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1,currentArrayPosition.getY() + 2));
        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1,currentArrayPosition.getY() + 2));
        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1,currentArrayPosition.getY() - 2));
        possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1,currentArrayPosition.getY() - 2));

        possibleMovesVectors.addAll(possibleAttackVectors);
    }
}
