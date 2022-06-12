package com.ourshipsgame.utils;

import com.ourshipsgame.chess_pieces.*;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.handlers.Constant;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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

        boolean endangeringKing = false;

        boolean savingKing = false;

        boolean isMyPlayerBlack = myPlayerColor.equals(Player.PlayerColor.BLACK);

        boolean myPlayerSimIsOnEven = initialDepth % 2 == 0;

        boolean isEvenDepth = depthLeft % 2 == 0;

        SimulationChess chessToMove = myDepthBoard.getLocationByArrayPosition(MyMove.getMoveLocation()).getChess();

        SimulationChess chessOrLocDestination = myDepthBoard.getLocationByArrayPosition(MyMove.getMoveDestination()).getChess();

        Player.PlayerColor myColor = chessToMove.getPlayerColor();

        if (chessOrLocDestination == null) {

            Vector2i myPosition = chessToMove.getCurrentLocation().getArrayPosition();
            boolean underAttack;

            if (myColor.equals(Player.PlayerColor.WHITE)) {
                underAttack = Arrays.stream(myDepthBoard.getBlackCheeses())
                        .anyMatch(simulationChess -> simulationChess.getPossibleAttackVectors()
                                .stream()
                                .anyMatch(vector2i -> vector2i.getX() == myPosition.getX() && vector2i.getY() == myPosition.getY())
                        );
            } else {
                underAttack = Arrays.stream(myDepthBoard.getWhiteCheeses())
                        .anyMatch(simulationChess -> simulationChess.getPossibleAttackVectors()
                                .stream()
                                .anyMatch(vector2i -> vector2i.getX() == myPosition.getX() && vector2i.getY() == myPosition.getY())
                        );
            }

            if (underAttack) {

                SimulationBoard board = new SimulationBoard(myDepthBoard.getWhiteCheeses(), myDepthBoard.getBlackCheeses());

                if (chessToMove.getClazz().getName().equals("com.ourshipsgame.chess_pieces.King")) {

                    if (myColor.equals(Player.PlayerColor.WHITE)) {

                        board.getWhiteCheeses()[Constant.ChessPiecesInArray.King.ordinal()]
                                .moveChess(board.getLocationByArrayPosition(MyMove.getMoveDestination()));

                        board.evaluateAllMoves();

                        Vector2i kingPosition = board.getWhiteKing().getCurrentLocation().getArrayPosition();

                        endangeringKing = Arrays.stream(board.getBlackCheeses())
                                .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                        .stream()
                                        .anyMatch(enemyAttack -> kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY())
                                );

                    } else {

                        board.getBlackCheeses()[Constant.ChessPiecesInArray.King.ordinal()]
                                .moveChess(board.getLocationByArrayPosition(MyMove.getMoveDestination()));

                        board.evaluateAllMoves();

                        Vector2i kingPosition = board.getBlackKing().getCurrentLocation().getArrayPosition();

                        endangeringKing = Arrays.stream(board.getWhiteCheeses())
                                .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                        .stream()
                                        .anyMatch(enemyAttack -> kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY())
                                );
                    }

                    myMinMaxScore = King.getValue() * 2;

                    if (endangeringKing)
                        myMinMaxScore *= -1;
                    else
                        savingKing = true;


                } else {

                    if (myColor.equals(Player.PlayerColor.WHITE)) {

                        for (int i = 0; i < 16; i++) {

                            if (board.getWhiteCheeses()[i].getCurrentLocation() == null)
                                continue;

                            Vector2i arrayPosition = board.getWhiteCheeses()[i].getCurrentLocation().getArrayPosition();

                            if (myPosition.getX() == arrayPosition.getX() && myPosition.getY() == arrayPosition.getY()) {

                                board.getWhiteCheeses()[i]
                                        .moveChess(board.getLocationByArrayPosition(MyMove.getMoveDestination()));

                                board.evaluateAllMoves();

                                break;
                            }
                        }

                        Vector2i kingPosition = board.getWhiteKing().getCurrentLocation().getArrayPosition();

                        endangeringKing = Arrays.stream(board.getBlackCheeses())
                                .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                        .stream()
                                        .anyMatch(enemyAttack -> kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY())
                                );


                    } else {

                        for (int i = 0; i < 16; i++) {

                            if (board.getBlackCheeses()[i].getCurrentLocation() == null)
                                continue;

                            Vector2i arrayPosition = board.getBlackCheeses()[i].getCurrentLocation().getArrayPosition();

                            if (myPosition.getX() == arrayPosition.getX() && myPosition.getY() == arrayPosition.getY()) {

                                board.getBlackCheeses()[i]
                                        .moveChess(board.getLocationByArrayPosition(MyMove.getMoveDestination()));

                                board.evaluateAllMoves();

                                break;
                            }
                        }

                        Vector2i kingPosition = board.getBlackKing().getCurrentLocation().getArrayPosition();

                        endangeringKing = Arrays.stream(board.getWhiteCheeses())
                                .anyMatch(simulatedChess -> simulatedChess.getPossibleAttackVectors()
                                        .stream()
                                        .anyMatch(enemyAttack -> kingPosition.getX() == enemyAttack.getX() && kingPosition.getY() == enemyAttack.getY())
                                );


                    }

                    switch (chessToMove.getClazz().getName()) {
                        case "com.ourshipsgame.chess_pieces.Bishop" -> myMinMaxScore = Bishop.getValue() / 2;

                        case "com.ourshipsgame.chess_pieces.King" -> {
                            myMinMaxScore = King.getValue() * 2;
                            savingKing = true;
                        }

                        case "com.ourshipsgame.chess_pieces.Knight" -> myMinMaxScore = Knight.getValue() / 2;

                        case "com.ourshipsgame.chess_pieces.Pawn" -> myMinMaxScore = Pawn.getValue() / 2;

                        case "com.ourshipsgame.chess_pieces.Queen" -> myMinMaxScore = Queen.getValue() / 2;

                        case "com.ourshipsgame.chess_pieces.Rook" -> myMinMaxScore = Rook.getValue() / 2;
                    }

                    if (endangeringKing)
                        myMinMaxScore -= 900;

                }

            } else {
                if (chessToMove.getClazz().getName().equals("com.ourshipsgame.chess_pieces.King"))
                    myMinMaxScore = -30;
                else
                    myMinMaxScore = 0;
            }

        } else {
            boolean savingKingFromMat = false;
            boolean savingKingByKing = false;
            boolean addPoints = !myColor.equals(Player.PlayerColor.WHITE) || !isMyPlayerBlack;

            SimulationChess myKing = myColor.equals(Player.PlayerColor.WHITE) ? myDepthBoard.getWhiteKing() : myDepthBoard.getBlackKing();
            Vector2i kingPosition = myKing.getCurrentLocation().getArrayPosition();

            if (chessToMove == myKing && myKing.isChecked()) {
                if (chessOrLocDestination.getPossibleAttackVectors().stream()
                        .anyMatch(vector2i -> kingPosition.getX() == vector2i.getX() && kingPosition.getY() == vector2i.getY())) {
                    savingKingByKing = true;
                    savingKingFromMat = true;
                }
            } else if (myKing.isChecked()) {
                if (chessOrLocDestination.getPossibleAttackVectors().stream()
                        .anyMatch(vector2i -> kingPosition.getX() == vector2i.getX() && kingPosition.getY() == vector2i.getY())) {
                    savingKingFromMat = true;
                }
            }

            switch (chessOrLocDestination.getClazz().getName()) {

                case "com.ourshipsgame.chess_pieces.Bishop" -> myMinMaxScore = Bishop.getValue();

                case "com.ourshipsgame.chess_pieces.King" -> {
                    myMinMaxScore = King.getValue() / 5;
                    checkingKing = true;
                }

                case "com.ourshipsgame.chess_pieces.Knight" -> myMinMaxScore = Knight.getValue();

                case "com.ourshipsgame.chess_pieces.Pawn" -> myMinMaxScore = Pawn.getValue();

                case "com.ourshipsgame.chess_pieces.Queen" -> myMinMaxScore = Queen.getValue();

                case "com.ourshipsgame.chess_pieces.Rook" -> myMinMaxScore = Rook.getValue();

                default -> myMinMaxScore = 0;

            }

            if (addPoints) {
                if (savingKingFromMat)
                    myMinMaxScore *= 10;
                if (savingKingByKing)
                    myMinMaxScore /= 2;
            }

            if (!addPoints)
                myMinMaxScore *= -1;

        }

        setSumOfMinMax(sumOfMinMax + myMinMaxScore);

        if (endangeringKing)
            return this;

        if (savingKing)
            return this;

        if (sumOfMinMax > 200)
            return this;

        if (sumOfMinMax < -30)
            return this;

        if (checkingKing)
            return this;

        if (depthLeft == 0)
            return this;

        chessToMove.moveChess(myDepthBoard.getLocationByArrayPosition(MyMove.getMoveDestination()));

        myDepthBoard.evaluateAllMoves();

        ArrayList<MinMaxMove> allPossibleMoves = new ArrayList<>();

        AtomicInteger randomizer = new AtomicInteger(5);

        if (myPlayerSimIsOnEven && isEvenDepth) {

            if (isMyPlayerBlack) {

                getRandomizer(randomizer, myDepthBoard.getBlackCheeses());

                Arrays.stream(myDepthBoard.getBlackCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess, randomizer.get())
                        );

            } else {

                getRandomizer(randomizer, myDepthBoard.getWhiteCheeses());

                Arrays.stream(myDepthBoard.getWhiteCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess, randomizer.get())
                        );
            }

        } else {

            if (isMyPlayerBlack) {

                getRandomizer(randomizer, myDepthBoard.getWhiteCheeses());

                Arrays.stream(myDepthBoard.getWhiteCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess, randomizer.get())
                        );
            } else {

                getRandomizer(randomizer, myDepthBoard.getBlackCheeses());

                Arrays.stream(myDepthBoard.getBlackCheeses())
                        .filter(simulationChess -> !simulationChess.isDestroyed())
                        .forEach(simulationChess ->
                                createNewMinMaxMoves(allPossibleMoves, simulationChess, randomizer.get())
                        );
            }

        }

        ArrayList<MinMax> deeperMinMaxes = (ArrayList<MinMax>) createDeeperMinMaxes(allPossibleMoves);

        ArrayList<MinMax> childrenMinMaxes = new ArrayList<>();


        if (initialDepth > 4 && initialDepth.equals(depthLeft + 2)) {

            Thread[] minMaxThreads = new Thread[deeperMinMaxes.size()];

            AtomicInteger index = new AtomicInteger(0);

            deeperMinMaxes.forEach(minMax -> {
                minMaxThreads[index.get()] = new Thread(() ->
                        childrenMinMaxes.add(minMax.findBestMinMax())
                );
                minMaxThreads[index.get()].setPriority(9);
                minMaxThreads[index.get()].start();
                index.getAndIncrement();
            });

            index.set(0);

            for (int i = 0; i < deeperMinMaxes.size(); i++) {
                try {
                    minMaxThreads[index.get()].join();
                    index.getAndIncrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else {

            deeperMinMaxes.forEach(minMax -> childrenMinMaxes.add(minMax.findBestMinMax()));

        }

        return childrenMinMaxes.stream()
                .max(Comparator.comparingInt(o -> o.sumOfMinMax))
                .orElse(this);

    }

    private void getRandomizer(AtomicInteger randomizer, SimulationChess[] cheeses) {
        long count = Arrays.stream(cheeses)
                .filter(simulationChess -> !simulationChess.isDestroyed())
                .count();

        if (count < 9)
            randomizer.set(3);
    }

    private void createNewMinMaxMoves(ArrayList<MinMaxMove> allPossibleMoves, SimulationChess simulationChess, int randomizer) {
        allPossibleMoves
                .addAll(simulationChess.getPossibleAttackVectors()
                        .stream()
                        .map(moveDest -> new MinMaxMove(simulationChess.getCurrentLocation().getArrayPosition(), moveDest))
                        .collect(Collectors.toList())
                );
        allPossibleMoves
                .addAll(simulationChess.getPossibleMovesVectors()
                        .stream()
                        .filter(move -> random.nextInt(10) % randomizer == 0)
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
