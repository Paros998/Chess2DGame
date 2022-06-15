package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.GameBoard.BoardLocations;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.utils.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Pawn extends Chess {
    private boolean firstMove;
    private static final List<BoardLocations> whiteChangeLocations = populateList(false);
    private static final List<BoardLocations> blackChangeLocations = populateList(true);

    private static List<BoardLocations> populateList(boolean color) {
        ArrayList<BoardLocations> boardLocations = new ArrayList<>();

        if(color){
            boardLocations.add(BoardLocations.A1);
            boardLocations.add(BoardLocations.B1);
            boardLocations.add(BoardLocations.C1);
            boardLocations.add(BoardLocations.D1);
            boardLocations.add(BoardLocations.E1);
            boardLocations.add(BoardLocations.F1);
            boardLocations.add(BoardLocations.G1);
            boardLocations.add(BoardLocations.H1);
        }
        boardLocations.add(BoardLocations.A8);
        boardLocations.add(BoardLocations.B8);
        boardLocations.add(BoardLocations.C8);
        boardLocations.add(BoardLocations.D8);
        boardLocations.add(BoardLocations.E8);
        boardLocations.add(BoardLocations.F8);
        boardLocations.add(BoardLocations.G8);
        boardLocations.add(BoardLocations.H8);

        return boardLocations;

    }

    public Pawn(Texture chessTexture, BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
        firstMove = true;
    }

    @Override
    public boolean moveChess(BoardLocations newPos, float soundVolume) {
        if(firstMove)
            firstMove = false;
        return super.moveChess(newPos, soundVolume);
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public boolean checkIfReachedEnd() {

        if(player.getColor().equals(Player.PlayerColor.WHITE))
            return whiteChangeLocations.stream()
                    .anyMatch(location -> location.equals(currentLocation));

        return blackChangeLocations.stream()
                .anyMatch(location -> location.equals(currentLocation));
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    @Override
    protected void filterMoves(GameBoard gameBoard, Chess[] whiteCheeses, Chess[] blackCheeses) {
        super.filterMoves(gameBoard, whiteCheeses, blackCheeses);

        Predicate<? super Vector2i> cannotJumpVerticallyOver = vector2i -> checkIfNotCrossedWithChessVertically(vector2i,gameBoard,currentLocation);

        possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                .filter(cannotJumpVerticallyOver)
                .collect(Collectors.toList());

        possibleMovesAndAttacksAsVectors.clear();
        possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
        possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);
    }

    public Integer getStrength() {
        return 10;
    }

    public static Integer getValue() {
        return 10;
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
