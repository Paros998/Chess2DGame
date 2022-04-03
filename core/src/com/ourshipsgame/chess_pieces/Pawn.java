package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.utils.Vector2i;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Pawn extends Chess {
    private boolean firstMove;

    public Pawn(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
        firstMove = true;
    }

    @Override
    public void moveChess(GameBoard.BoardLocations newPos, float soundVolume) {
        if(firstMove)
            firstMove = false;
        super.moveChess(newPos, soundVolume);
    }

    @Override
    protected void filterMoves(GameBoard gameBoard) {
        super.filterMoves(gameBoard);

        Predicate<? super Vector2i> cannotJumpVerticallyOver = vector2i -> checkIfNotCrossedWithChessVertically(vector2i,gameBoard,currentLocation);

        possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                .filter(cannotJumpVerticallyOver)
                .collect(Collectors.toList());

        possibleMovesAndAttacksAsVectors.clear();
        possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
        possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);
    }


    @Override
    protected void calculatePossibleMoves(GameBoard gameBoard) {
        Player.PlayerColor playerColor = player.getColor();
        Vector2i currentArrayPosition = currentLocation.getArrayPosition();

        switch (playerColor) {
            case BLACK -> {
                if (firstMove) {
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - 2));
                }
                possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - 1));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() - 1));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() - 1));
            }
            case WHITE -> {
                if (firstMove) {
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + 2));
                }
                possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + 1));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() + 1));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() + 1));
            }
        }
    }
}
