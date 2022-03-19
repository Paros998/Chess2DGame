package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.utils.Vector2i;

public class King extends Chess {
    private boolean check;
    private boolean mate;

    public King(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
    }


    @Override
    protected void calculatePossibleMoves(GameBoard gameBoard) {

        Vector2i currentArrayPosition = currentLocation.getArrayPosition();

        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() + 1));
        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() - 1));
        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() + 1));
        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() - 1));
        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY()));
        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY()));
        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + 1));
        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - 1));


        possibleAttackVectors.addAll(possibleMovesVectors);
    }

    public boolean hasLost() {
        return check & mate;
    }
}
