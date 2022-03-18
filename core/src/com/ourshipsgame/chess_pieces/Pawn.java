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
    protected void filterMoves(GameBoard gameBoard) {
        super.filterMoves(gameBoard);

        GameBoard.BoardLocations[][] board = gameBoard.getBoard();

        int checkingDirection = player.getColor().equals(Player.PlayerColor.WHITE) ? -1 : 1;

        Predicate<? super Vector2i> jumpOverPredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY() + checkingDirection].getChess() != null;

        possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                .dropWhile(jumpOverPredicate)
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
