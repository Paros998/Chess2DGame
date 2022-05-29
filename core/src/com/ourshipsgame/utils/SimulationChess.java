package com.ourshipsgame.utils;

import com.ourshipsgame.chess_pieces.Chess;
import com.ourshipsgame.chess_pieces.King;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.handlers.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ourshipsgame.game.Player.PlayerColor.BLACK;

public class SimulationChess {
    private final Player.PlayerColor playerColor;
    private SimulationBoardLocation currentLocation;
    private final ArrayList<Vector2i> possibleMovesAndAttacksAsVectors = new ArrayList<>();
    private ArrayList<Vector2i> possibleMovesVectors = new ArrayList<>();
    private ArrayList<Vector2i> possibleAttackVectors = new ArrayList<>();
    private Class<? extends Chess> clazz;
    private boolean isDestroyed;
    private boolean isChecked = false;
    private boolean isMated = false;
    private boolean firstMove = false;
    private boolean wasCheckedTurnAgo = false;

    public SimulationChess(Player.PlayerColor playerColor,
                           SimulationBoardLocation location,
                           Class<? extends Chess> clazz,
                           boolean isDestroyed,
                           boolean firstMove) {
        this.playerColor = playerColor;
        this.currentLocation = location;
        this.clazz = clazz;
        this.isDestroyed = isDestroyed;
        this.firstMove = firstMove;
    }

    public SimulationChess(Player.PlayerColor playerColor,
                           SimulationBoardLocation location,
                           boolean wasCheckedTurnAgo,
                           boolean isChecked) {
        this.playerColor = playerColor;
        this.currentLocation = location;
        this.clazz = King.class;
        this.isChecked = isChecked;
        this.wasCheckedTurnAgo = wasCheckedTurnAgo;
    }

    public SimulationChess(Player.PlayerColor playerColor,
                           SimulationBoardLocation location,
                           Class<? extends Chess> clazz,
                           boolean isDestroyed) {
        this.playerColor = playerColor;
        this.currentLocation = location;
        this.clazz = clazz;
        this.isDestroyed = isDestroyed;
    }

    public void evaluateMoves(SimulationBoard gameBoard) {
        if (!isDestroyed) {
            possibleMovesAndAttacksAsVectors.clear();
            possibleMovesVectors.clear();
            possibleAttackVectors.clear();
            calculatePossibleMoves();
            filterMoves(gameBoard);

        }
    }

    public void checkKingCondition(SimulationChess[] enemyCheeses, SimulationBoard board) {

        if (clazz.equals(King.class)) {
            isChecked = false;

            Vector2i currentVector = currentLocation.getArrayPosition();

            Arrays.stream(enemyCheeses).forEach(chess -> {
                if (!chess.isDestroyed())
                    chess.getPossibleAttackVectors().forEach(vector2i -> {
                        if (vector2i.getX() == currentVector.getX() && vector2i.getY() == currentVector.getY())
                            isChecked = true;
                    });
            });

            if (isChecked) {

//            possibleAttackVectors.clear();

                possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                        .filter(attack -> {
                            boolean canAttack = true;
                            SimulationBoard kingSimulationBoard;

                            if (playerColor.equals(Player.PlayerColor.WHITE)) {

                                kingSimulationBoard = new SimulationBoard(board.getWhiteCheeses(), enemyCheeses);

                                kingSimulationBoard.getWhiteCheeses()[Constant.ChessPiecesInArray.King.ordinal()]
                                        .moveChess(kingSimulationBoard.getBoard()[attack.getX()][attack.getY()]);

                                kingSimulationBoard.evaluateAllMoves();

                                boolean isKingAttacked = Arrays.stream(kingSimulationBoard.getBlackCheeses())
                                        .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                                .stream()
                                                .anyMatch(enemyAttack -> {
                                                    Vector2i kingPosition = kingSimulationBoard.getWhiteCheeses()[Constant.ChessPiecesInArray.King.ordinal()].getCurrentLocation().getArrayPosition();

                                                    return kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY();

                                                })
                                        );

                                if (isKingAttacked)
                                    canAttack = false;

                            } else {

                                kingSimulationBoard = new SimulationBoard(enemyCheeses, board.getBlackCheeses());

                                kingSimulationBoard.getBlackCheeses()[Constant.ChessPiecesInArray.King.ordinal()]
                                        .moveChess(kingSimulationBoard.getBoard()[attack.getX()][attack.getY()]);

                                kingSimulationBoard.evaluateAllMoves();

                                boolean isKingAttacked = Arrays.stream(kingSimulationBoard.getWhiteCheeses())
                                        .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                                .stream()
                                                .anyMatch(enemyAttack -> {
                                                    Vector2i kingPosition = kingSimulationBoard.getBlackCheeses()[Constant.ChessPiecesInArray.King.ordinal()].getCurrentLocation().getArrayPosition();

                                                    return kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY();

                                                })
                                        );

                                if (isKingAttacked)
                                    canAttack = false;

                            }

                            return canAttack;

                        }).collect(Collectors.toList());

                possibleMovesAndAttacksAsVectors.clear();

                possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);

            }
        }

        if (possibleMovesAndAttacksAsVectors.isEmpty() && isChecked && wasCheckedTurnAgo)
            isMated = true;

        wasCheckedTurnAgo = isChecked;

    }

    private void calculatePossibleMoves() {

        Vector2i currentArrayPosition = currentLocation.getArrayPosition();

        switch (clazz.getName()) {

            case "com.ourshipsgame.chess_pieces.Bishop" -> {
                for (int i = 0; i < 8; i++) {
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + i, currentArrayPosition.getY() + i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - i, currentArrayPosition.getY() - i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - i, currentArrayPosition.getY() + i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + i, currentArrayPosition.getY() - i));
                }

                possibleAttackVectors.addAll(possibleMovesVectors);
            }

            case "com.ourshipsgame.chess_pieces.King" -> {
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

            case "com.ourshipsgame.chess_pieces.Knight" -> {
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 2, currentArrayPosition.getY() + 1));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 2, currentArrayPosition.getY() - 1));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 2, currentArrayPosition.getY() + 1));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 2, currentArrayPosition.getY() - 1));

                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() + 2));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() + 2));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() - 2));
                possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() - 2));

                possibleMovesVectors.addAll(possibleAttackVectors);

            }

            case "com.ourshipsgame.chess_pieces.Pawn" -> {

                if (playerColor.equals(BLACK)) {
                    if (firstMove) {
                        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - 2));
                    }
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - 1));
                    possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() - 1));
                    possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() - 1));
                } else {
                    if (firstMove) {
                        possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + 2));
                    }
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + 1));
                    possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() - 1, currentArrayPosition.getY() + 1));
                    possibleAttackVectors.add(new Vector2i(currentArrayPosition.getX() + 1, currentArrayPosition.getY() + 1));

                }

            }

            case "com.ourshipsgame.chess_pieces.Queen" -> {
                for (int i = 0; i < 8; i++) {
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + i, currentArrayPosition.getY() + i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - i, currentArrayPosition.getY() - i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - i, currentArrayPosition.getY() + i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + i, currentArrayPosition.getY() - i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + i, currentArrayPosition.getY()));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - i, currentArrayPosition.getY()));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - i));
                }

                possibleAttackVectors.addAll(possibleMovesVectors);
            }

            case "com.ourshipsgame.chess_pieces.Rook" -> {
                for (int i = 0; i < 8; i++) {
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() + i, currentArrayPosition.getY()));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX() - i, currentArrayPosition.getY()));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() + i));
                    possibleMovesVectors.add(new Vector2i(currentArrayPosition.getX(), currentArrayPosition.getY() - i));
                }

                possibleAttackVectors.addAll(possibleMovesVectors);
            }

        }

    }

    private void filterMoves(SimulationBoard gameBoard) {

        SimulationBoardLocation[][] board = gameBoard.getBoard();

        SimulationChess[] enemyCheeses = getPlayerColor().equals(BLACK) ? gameBoard.getWhiteCheeses() : gameBoard.getBlackCheeses();

        Predicate<Vector2i> movePredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() == null;

        Predicate<Vector2i> predicateUnderAttack = vector2i ->
        {
            for (int i = 0; i < 16; i++)
                if (!enemyCheeses[i].isDestroyed)
                    return enemyCheeses[i].getPossibleMovesAndAttacksAsVectors().stream()
                            .anyMatch(enemyVector2 -> vector2i.getX() == enemyVector2.getX() && vector2i.getY() == enemyVector2.getY());
            return true;
        };

        Predicate<Vector2i> attackPredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() != null &&
                        board[vector2i.getX()][vector2i.getY()].getChess().getPlayerColor() != getPlayerColor();

        Predicate<Vector2i> cannotMoveDiagonallyOver = vector2i -> checkIfNotCrossedWithChessDiagonally(vector2i, gameBoard, currentLocation);

        Predicate<Vector2i> xOffsetPredicate = vector2i -> vector2i.getX() >= 0 && vector2i.getX() <= 7;
        Predicate<Vector2i> yOffsetPredicate = vector2i -> vector2i.getY() >= 0 && vector2i.getY() <= 7;

        Predicate<Vector2i> ableToMovePredicate = movePredicate.and(predicateUnderAttack);
        Predicate<Vector2i> ableToAttackPredicate = attackPredicate.and(predicateUnderAttack);

        Predicate<Vector2i> boardOffsetPredicate = xOffsetPredicate.and(yOffsetPredicate);

        Predicate<Vector2i> cannotJumpVerticallyOver = vector2i -> checkIfNotCrossedWithChessVertically(vector2i, gameBoard, currentLocation);

        Predicate<Vector2i> cannotMoveHorizontallyOver = vector2i -> checkIfNotCrossedWithChessHorizontally(vector2i, gameBoard, currentLocation);
        Predicate<Vector2i> cannotMoveVerticallyOver = vector2i -> checkIfNotCrossedWithChessVertically(vector2i, gameBoard, currentLocation);


        switch (clazz.getName()) {

            case "com.ourshipsgame.chess_pieces.Bishop" -> {

                possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(movePredicate)
                        .filter(cannotMoveDiagonallyOver)
                        .collect(Collectors.toList());

                possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(attackPredicate)
                        .filter(cannotMoveDiagonallyOver)
                        .collect(Collectors.toList());
            }

            case "com.ourshipsgame.chess_pieces.King" -> {
                //TODO fix predicateUnderAttack
                possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(ableToMovePredicate)
                        .collect(Collectors.toList());

                possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(ableToAttackPredicate)
                        .collect(Collectors.toList());
            }

            case "com.ourshipsgame.chess_pieces.Knight" -> {

                possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(movePredicate)
                        .collect(Collectors.toList());

                possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(attackPredicate)
                        .collect(Collectors.toList());

            }

            case "com.ourshipsgame.chess_pieces.Pawn" -> {

                possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(movePredicate)
                        .filter(cannotJumpVerticallyOver)
                        .collect(Collectors.toList());

                possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(attackPredicate)
                        .filter(cannotJumpVerticallyOver)
                        .collect(Collectors.toList());

            }

            case "com.ourshipsgame.chess_pieces.Queen" -> {

                possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(movePredicate)
                        .filter(cannotMoveHorizontallyOver)
                        .filter(cannotMoveVerticallyOver)
                        .filter(cannotMoveDiagonallyOver)
                        .collect(Collectors.toList());

                possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(attackPredicate)
                        .filter(cannotMoveHorizontallyOver)
                        .filter(cannotMoveVerticallyOver)
                        .filter(cannotMoveDiagonallyOver)
                        .collect(Collectors.toList());
            }

            case "com.ourshipsgame.chess_pieces.Rook" -> {

                possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(movePredicate)
                        .filter(cannotMoveHorizontallyOver)
                        .filter(cannotMoveVerticallyOver)
                        .collect(Collectors.toList());

                possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                        .filter(boardOffsetPredicate)
                        .filter(attackPredicate)
                        .filter(cannotMoveHorizontallyOver)
                        .filter(cannotMoveVerticallyOver)
                        .collect(Collectors.toList());
            }

        }

        possibleMovesAndAttacksAsVectors.clear();
        possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
        possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);

    }

    public boolean moveChess(SimulationBoardLocation newPos) {
        if (firstMove)
            firstMove = false;

        if (canMove(newPos)) {
            newPos.setChess(this);
            return true;
        }
        return false;

    }

    private boolean canMove(SimulationBoardLocation position) {
        return possibleMovesAndAttacksAsVectors.stream()
                .anyMatch(vector2i -> {
                    if (position.getChess() == null)
                        return vector2i.epsilonEquals(position.getArrayPosition());
                    return vector2i.epsilonEquals(position.getArrayPosition()) && !position.getChess().getClazz().equals(King.class);
                });
    }

    protected static boolean checkIfNotCrossedWithChessHorizontally(Vector2i vector2i, SimulationBoard gameBoard, SimulationBoardLocation currentLocation) {
        int currentX = currentLocation.getArrayPosition().getX();
        boolean checkRight = vector2i.getX() > currentX;

        if (currentLocation.getArrayPosition().getY() != vector2i.getY())
            return true;

        if (checkRight) {
            int difference = vector2i.getX() - currentX;
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[currentX + i][vector2i.getY()].getChess() != null)
                    return false;

        } else {
            int difference = currentX - vector2i.getX();
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[currentX - i][vector2i.getY()].getChess() != null)
                    return false;

        }
        return true;
    }

    protected static boolean checkIfNotCrossedWithChessDiagonally(Vector2i vector2i, SimulationBoard gameBoard, SimulationBoardLocation currentLocation) {
        int currentX = currentLocation.getArrayPosition().getX();
        int currentY = currentLocation.getArrayPosition().getY();

        if (currentX == vector2i.getX())
            return true;

        if (currentY == vector2i.getY())
            return true;

        boolean checkRight = vector2i.getX() > currentX;
        boolean checkTop = vector2i.getY() > currentY;

        int differenceX;

        if (checkRight) {
            differenceX = vector2i.getX() - currentX;

            if (checkTop) {
                for (int i = 1; i < differenceX; i++) {
                    if (currentX + i > 7 || currentY + i > 7)
                        break;
                    if (gameBoard.getBoard()[currentX + i][currentY + i].getChess() != null)
                        return false;
                }

            } else
                for (int i = 1; i < differenceX; i++) {
                    if (currentX + i > 7 || currentY - i < 0)
                        break;
                    if (gameBoard.getBoard()[currentX + i][currentY - i].getChess() != null)
                        return false;
                }

        } else {
            differenceX = currentX - vector2i.getX();

            if (checkTop) {
                for (int i = 1; i < differenceX; i++) {
                    if (currentX - i < 0 || currentY + i > 7)
                        break;
                    if (gameBoard.getBoard()[currentX - i][currentY + i].getChess() != null)
                        return false;
                }
            } else
                for (int i = 1; i < differenceX; i++) {
                    if (currentX - i < 0 || currentY - i < 0)
                        break;
                    if (gameBoard.getBoard()[currentX - i][currentY - i].getChess() != null)
                        return false;
                }

        }

        return true;
    }

    protected static boolean checkIfNotCrossedWithChessVertically(Vector2i vector2i, SimulationBoard gameBoard, SimulationBoardLocation currentLocation) {
        int currentY = currentLocation.getArrayPosition().getY();
        boolean checkTop = vector2i.getY() > currentY;

        if (currentLocation.getArrayPosition().getX() != vector2i.getX())
            return true;

        if (checkTop) {
            int difference = vector2i.getY() - currentY;
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[vector2i.getX()][currentY + i].getChess() != null)
                    return false;

        } else {
            int difference = currentY - vector2i.getY();
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[vector2i.getX()][currentY - i].getChess() != null)
                    return false;

        }
        return true;
    }


    public SimulationChess setCurrentLocation(SimulationBoardLocation currentLocation) {
        this.currentLocation = currentLocation;
        return this;
    }

    public SimulationChess setClazz(Class<Chess> clazz) {
        this.clazz = clazz;
        return this;
    }

    public SimulationChess setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
        return this;
    }

    public SimulationBoardLocation getCurrentLocation() {
        return currentLocation;
    }

    public ArrayList<Vector2i> getPossibleMovesAndAttacksAsVectors() {
        return possibleMovesAndAttacksAsVectors;
    }

    public ArrayList<Vector2i> getPossibleMovesVectors() {
        return possibleMovesVectors;
    }

    public ArrayList<Vector2i> getPossibleAttackVectors() {
        return possibleAttackVectors;
    }

    public Class<? extends Chess> getClazz() {
        return clazz;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public Player.PlayerColor getPlayerColor() {
        return playerColor;
    }

    public boolean isMated() {
        return isMated;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public boolean isWasCheckedTurnAgo() {
        return wasCheckedTurnAgo;
    }
}
