package com.ourshipsgame.utils;

import com.ourshipsgame.chess_pieces.*;
import com.ourshipsgame.game.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MinMax {
    private static final Random random = new Random(System.currentTimeMillis());
    private static Integer numOfMinMaxes = 0;
    private final MinMaxMove FirstMove;
    private final Integer initialDepth;
    private final SimulationBoard myDepthBoard;
    private final Integer depthLeft;
    private final MinMaxMove MyMove;
    private final Player.PlayerColor myPlayerColor;
    private Integer myMinMaxScore;
    private Integer sumOfMinMax;

    public MinMax(MinMaxMove firstMove,
                  Integer initialDepth,
                  MinMaxMove myMove,
                  SimulationBoard myDepthBoard,
                  Integer depthLeft,
                  Player.PlayerColor myPlayerColor,
                  Integer actualSumOfMinMax) {

        FirstMove = firstMove;
        this.initialDepth = initialDepth;
        MyMove = myMove;
        this.myDepthBoard = myDepthBoard;
        this.depthLeft = depthLeft;
        this.myPlayerColor = myPlayerColor;
        sumOfMinMax = actualSumOfMinMax;

        numOfMinMaxes++;
    }

    public MinMax findBestMinMax() {

        boolean checkingKing = false;

        boolean isMyPlayerBlack = myPlayerColor.equals(Player.PlayerColor.BLACK);

        boolean myPlayerSimIsOnEven = initialDepth % 2 == 0;

        boolean isEvenDepth = depthLeft % 2 == 0;

        SimulationChess chessToMove = myDepthBoard.getLocationByArrayPosition(MyMove.getMoveLocation()).getChess();

        SimulationChess chessOrLocDestination = myDepthBoard.getLocationByArrayPosition(MyMove.getMoveDestination()).getChess();

        if (chessOrLocDestination == null)
            myMinMaxScore = 0;
        else {
            boolean addPoints = !chessToMove.getPlayerColor().equals(Player.PlayerColor.WHITE) || !isMyPlayerBlack;

            switch (chessOrLocDestination.getClazz().getName()) {

                case "com.ourshipsgame.chess_pieces.Bishop" -> {
                    myMinMaxScore = Bishop.getValue();
                }

                case "com.ourshipsgame.chess_pieces.King" -> {
                    myMinMaxScore = King.getValue();
                    checkingKing = true;
                }

                case "com.ourshipsgame.chess_pieces.Knight" -> {
                    myMinMaxScore = Knight.getValue();
                }

                case "com.ourshipsgame.chess_pieces.Pawn" -> {
                    myMinMaxScore = Pawn.getValue();
                }

                case "com.ourshipsgame.chess_pieces.Queen" -> {
                    myMinMaxScore = Queen.getValue();
                }

                case "com.ourshipsgame.chess_pieces.Rook" -> {
                    myMinMaxScore = Rook.getValue();
                }
                default -> {
                    myMinMaxScore = 0;
                }

            }

            if (!addPoints)
                myMinMaxScore *= -1;

        }

        setSumOfMinMax(sumOfMinMax + myMinMaxScore);

        if (sumOfMinMax > 100)
            return this;

        if (sumOfMinMax < -100)
            return this;

        if (checkingKing)
            return this;

        if (depthLeft == 0)
            return this;

        chessToMove.moveChess(myDepthBoard.getLocationByArrayPosition(MyMove.getMoveDestination()));

        myDepthBoard.evaluateAllMoves();

        ArrayList<MinMaxMove> allPossibleMoves = new ArrayList<>();

        if (myPlayerSimIsOnEven && isEvenDepth) {

            if (isMyPlayerBlack) {

                Arrays.stream(myDepthBoard.getBlackCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess)
                        );

            } else {

                Arrays.stream(myDepthBoard.getWhiteCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess)
                        );
            }

        } else {

            if (isMyPlayerBlack) {

                Arrays.stream(myDepthBoard.getWhiteCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess)
                        );
            } else {

                Arrays.stream(myDepthBoard.getBlackCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess)
                        );
            }

        }

        ArrayList<MinMax> deeperMinMaxes = (ArrayList<MinMax>) createDeeperMinMaxes(allPossibleMoves);

        ArrayList<MinMax> childrenMinMaxes = new ArrayList<>();

        deeperMinMaxes.forEach(minMax -> childrenMinMaxes.add(minMax.findBestMinMax()));

        return childrenMinMaxes.stream().max((o1, o2) -> o1.sumOfMinMax - o2.sumOfMinMax).orElse(this);

    }

    private void createNewMinMaxMoves(ArrayList<MinMaxMove> allPossibleMoves, SimulationChess simulationChess) {
        allPossibleMoves
                .addAll(simulationChess.getPossibleAttackVectors()
                        .stream()
                        .map(moveDest -> new MinMaxMove(simulationChess.getCurrentLocation().getArrayPosition(), moveDest))
                        .collect(Collectors.toList())
                );
        allPossibleMoves
                .addAll(simulationChess.getPossibleMovesVectors()
                        .stream()
                        .filter(move -> random.nextInt(10) % 5 == 0)
                        .map(moveDest -> new MinMaxMove(simulationChess.getCurrentLocation().getArrayPosition(), moveDest))
                        .collect(Collectors.toList())
                );
    }

    private List<MinMax> createDeeperMinMaxes(List<MinMaxMove> possibleMinMaxesMoves) {

        return possibleMinMaxesMoves.stream()
                .map(minMaxMove -> new MinMax(
                        getFirstMove(),
                        initialDepth,
                        minMaxMove,
                        new SimulationBoard(myDepthBoard.getWhiteCheeses(), myDepthBoard.getBlackCheeses()),
                        depthLeft - 1,
                        myPlayerColor,
                        sumOfMinMax)
                )
                .collect(Collectors.toList());


    }

    public static List<MinMax> createFirstMinMaxes(List<MinMaxMove> possibleMinMaxesMoves,
                                                   Integer initialDepth,
                                                   SimulationBoard myDepthBoard,
                                                   Player.PlayerColor myPlayerColor,
                                                   Integer sumOfMinMax) {

        return possibleMinMaxesMoves.stream()
                .map(minMaxMove -> new MinMax(
                        minMaxMove,
                        initialDepth,
                        minMaxMove,
                        new SimulationBoard(myDepthBoard.getWhiteCheeses(), myDepthBoard.getBlackCheeses()),
                        initialDepth,
                        myPlayerColor,
                        sumOfMinMax)
                )
                .collect(Collectors.toList());

    }

    public Integer getSumOfMinMax() {
        return sumOfMinMax;
    }

    public Integer getMyMinMaxScore() {
        return myMinMaxScore;
    }

    public void setSumOfMinMax(Integer sumOfMinMax) {
        this.sumOfMinMax = sumOfMinMax;
    }

    public MinMaxMove getFirstMove() {
        return FirstMove;
    }

    public static Integer getNumOfMinMaxes() {
        return numOfMinMaxes;
    }

    public static void setNumOfMinMaxes(Integer numOfMinMaxes) {
        MinMax.numOfMinMaxes = numOfMinMaxes;
    }
}
