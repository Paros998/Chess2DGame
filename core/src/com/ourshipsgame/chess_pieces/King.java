package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.utils.SimulationBoard;
import com.ourshipsgame.utils.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class King extends Chess {
    private boolean isPatted = false;
    private boolean isChecked = false;
    private boolean wasCheckedTurnAgo = false;
    private boolean isMated = false;

    public King(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        super(chessTexture, location, manager);
    }

    public static Integer getValue() {
        return 900;
    }

    public Integer getStrength() {
        return 900;
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

    public void evaluateMoves(GameBoard gameBoard, Chess[] enemyCheeses) {
        if (!isDestroyed) {
            possibleMovesAndAttacksAsVectors.clear();
            possibleMovesVectors.clear();
            possibleAttackVectors.clear();
            calculatePossibleMoves(gameBoard);
            filterMoves(gameBoard, enemyCheeses);
            createMovesObjects(gameBoard);
        }
    }

    private void filterMoves(GameBoard gameBoard, Chess[] enemyCheeses) {
        GameBoard.BoardLocations[][] board = gameBoard.getBoard();

        Predicate<Vector2i> movePredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() == null;

        Predicate<Vector2i> predicateUnderAttack = vector2i ->
        {
            for (int i = 0; i < 16; i++)
                if (!enemyCheeses[i].isDestroyed)
                    if(enemyCheeses[i].getPossibleMovesAndAttacksAsVectors().stream()
                            .anyMatch(enemyVector2 -> vector2i.getX() == enemyVector2.getX() && vector2i.getY() == enemyVector2.getY()))
                        return false;
            return true;
        };

        Predicate<Vector2i> attackPredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() != null &&
                        board[vector2i.getX()][vector2i.getY()].getChess().getPlayer() != player;

        Predicate<Vector2i> xOffsetPredicate = vector2i -> vector2i.getX() >= 0 && vector2i.getX() <= 7;
        Predicate<Vector2i> yOffsetPredicate = vector2i -> vector2i.getY() >= 0 && vector2i.getY() <= 7;


        Predicate<Vector2i> ableToMovePredicate = movePredicate.and(predicateUnderAttack);
        Predicate<Vector2i> ableToAttackPredicate = attackPredicate.and(predicateUnderAttack);
        Predicate<Vector2i> offsetPredicate = xOffsetPredicate.and(yOffsetPredicate);

        possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                .filter(offsetPredicate)
                .filter(movePredicate)
                .filter(predicateUnderAttack)
                .collect(Collectors.toList());

        possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                .filter(offsetPredicate)
                .filter(attackPredicate)
                .filter(predicateUnderAttack)
                .collect(Collectors.toList());

        possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);
        possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
    }

    public void checkKingCondition(Chess[] enemyCheeses, GameBoard gameBoard) {

        isChecked = isMated = isPatted = false;
        Vector2i currentVector = currentLocation.getArrayPosition();
        AtomicReference<Chess> enemyCheckingChess = new AtomicReference<>(null);
        ArrayList<Vector2i> defendingMoves = new ArrayList<>();
        Chess[] myCheeses = getPlayer().getMyCheeses();

        Arrays.stream(enemyCheeses).forEach(chess -> {
            if (!chess.isDestroyed())
                chess.getPossibleAttackVectors().forEach(vector2i -> {
                    if (vector2i.getX() == currentVector.getX() && vector2i.getY() == currentVector.getY()) {
                        isChecked = true;
                        enemyCheckingChess.set(chess);
                    }
                });
        });

        if (isChecked) {

            possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                    .filter(attack -> {
                        boolean canAttack = true;
                        SimulationBoard board;

                        if (player.getColor().equals(Player.PlayerColor.WHITE)) {

                            board = new SimulationBoard(player.getMyCheeses(), enemyCheeses);

                            board.getWhiteCheeses()[Constant.ChessPiecesInArray.King.ordinal()]
                                    .moveChess(board.getBoard()[attack.getX()][attack.getY()]);

                            board.evaluateAllMoves();

                            Vector2i kingPosition = board.getWhiteCheeses()[Constant.ChessPiecesInArray.King.ordinal()].getCurrentLocation().getArrayPosition();

                            boolean isKingAttacked = Arrays.stream(board.getBlackCheeses())
                                    .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                            .stream()
                                            .anyMatch(enemyAttack -> kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY())
                                    );

                            if (isKingAttacked)
                                canAttack = false;

                        } else {

                            board = new SimulationBoard(enemyCheeses, player.getMyCheeses());

                            board.getBlackCheeses()[Constant.ChessPiecesInArray.King.ordinal()]
                                    .moveChess(board.getBoard()[attack.getX()][attack.getY()]);

                            board.evaluateAllMoves();

                            Vector2i kingPosition = board.getBlackCheeses()[Constant.ChessPiecesInArray.King.ordinal()].getCurrentLocation().getArrayPosition();


                            boolean isKingAttacked = Arrays.stream(board.getWhiteCheeses())
                                    .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                            .stream()
                                            .anyMatch(enemyAttack -> kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY())
                                    );

                            if (isKingAttacked)
                                canAttack = false;

                        }

                        return canAttack;

                    }).collect(Collectors.toList());

            possibleMovesAndAttacksAsVectors.clear();

            possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);
            possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
            createMovesObjects(gameBoard);

            Vector2i enemyVector = enemyCheckingChess.get().getCurrentLocation().getArrayPosition();

            Arrays.stream(myCheeses)
                    .filter(chess -> !(chess instanceof King))
                    .forEach(chess -> {
                        chess.getPossibleAttackVectors().forEach(
                                vector2i -> {
                                    if (vector2i.getX() == enemyVector.getX() && vector2i.getY() == enemyVector.getY())
                                        defendingMoves.add(vector2i);
                                }
                        );
                    });

        }


        if (wasCheckedTurnAgo && isChecked && player.isMadeMoveSinceKingIsChecked())
            isMated = true;

        if (possibleMovesAndAttacksAsVectors.isEmpty() && isChecked && defendingMoves.isEmpty())
            isMated = true;

//        if(possibleMovesAndAttacksAsVectors.isEmpty() && !isMated && !isChecked)
//            isPatted = true;

//        wasCheckedTurnAgo = isChecked;

    }

    public boolean isWasCheckedTurnAgo() {
        return wasCheckedTurnAgo;
    }

    public void setWasCheckedTurnAgo(boolean wasCheckedTurnAgo) {
        this.wasCheckedTurnAgo = wasCheckedTurnAgo;
    }

    public boolean isTie() {
        return isPatted;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isMated() {
        return isMated;
    }

    public void setMated(boolean mated) {
        isMated = mated;
    }
}
