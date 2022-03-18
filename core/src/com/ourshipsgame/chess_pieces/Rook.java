package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.utils.Vector2i;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Rook extends Chess{
    public Rook(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
    }

    @Override
    protected void filterMoves(GameBoard gameBoard) {
        super.filterMoves(gameBoard);

        GameBoard.BoardLocations[][] board = gameBoard.getBoard();

        Predicate<? super Vector2i> jumpOverPredicate;

//        possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
//                .collect(Collectors.toList());

        possibleMovesAndAttacksAsVectors.clear();
        possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
        possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);

    }

    @Override
    protected void calculatePossibleMoves(GameBoard gameBoard) {
        GameBoard.BoardLocations[][] board = gameBoard.getBoard();
        Vector2i currentArrayPosition = currentLocation.getArrayPosition();

        for (int i = 0; i < 8; i++) {
            possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + i, currentArrayPosition.getY()));
            possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - i, currentArrayPosition.getY()));
            possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + i));
            possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - i));
        }

    }

}
