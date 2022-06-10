package com.ourshipsgame.inteligentSystems;

import com.ourshipsgame.chess_pieces.Chess;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.utils.MinMax;
import com.ourshipsgame.utils.MinMaxMove;
import com.ourshipsgame.utils.SimulationBoard;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ourshipsgame.game.GameBoard.BoardLocations.getEnumByArrayPosition;

/**
 * Klasa odpowiadająca za podejmowanie decyzji przez komputer
 */
public class ComputerPlayerAi {

    private Player myPlayer;
    private final Chess[] enemyCheeses;
    private final Chess[] myCheeses;
    private Chess movableChess;
    private GameBoard.BoardLocations moveStart;
    private GameBoard.BoardLocations moveDestination;
    private final static Float minTurnTime = 1.5f;
    private final AtomicReference<Float> passedTurnTime = new AtomicReference<>(0.0f);
    private final AtomicBoolean isCalculating = new AtomicBoolean(false);
    private final AtomicBoolean isReadyToMove = new AtomicBoolean(false);

    /**
     * Konstruktor główny obiektu, który inicjuje i ustawia dane do obliczeń logiki
     *
     * @param enemyCheeses
     * @param myCheeses    array of AI cheeses
     */
    public ComputerPlayerAi(Chess[] enemyCheeses, Chess[] myCheeses) {
        this.enemyCheeses = enemyCheeses;
        this.myCheeses = myCheeses;
    }

    public void update(float rawTime) {
        passedTurnTime.set(passedTurnTime.get() + rawTime);
    }

    private MinMax findBestMinMax(int depth) {
        Random random = new Random(System.currentTimeMillis());

        ArrayList<MinMaxMove> allPossibleMoves = new ArrayList<>();

        SimulationBoard board = myPlayer.getColor().equals(Player.PlayerColor.WHITE) ? new SimulationBoard(myCheeses, enemyCheeses) : new SimulationBoard(enemyCheeses, myCheeses);

        Arrays.stream(myCheeses)
                .filter(chess -> !chess.isDestroyed())
                .forEach(simulationChess -> {
                            allPossibleMoves
                                    .addAll(simulationChess.getPossibleAttackVectors()
                                            .stream()
                                            .map(moveDest -> new MinMaxMove(simulationChess.getCurrentLocation().getArrayPosition(), moveDest))
                                            .collect(Collectors.toList())
                                    );
                            allPossibleMoves
                                    .addAll(simulationChess.getPossibleMovesVectors()
                                            .stream()
                                            .filter(move -> random.nextInt(10) % 2 == 0)
                                            .map(moveDest -> new MinMaxMove(simulationChess.getCurrentLocation().getArrayPosition(), moveDest))
                                            .collect(Collectors.toList())
                                    );
                        }

                );

        List<MinMax> firstMinMaxes = MinMax.createFirstMinMaxes(allPossibleMoves, depth, board, myPlayer.getColor(), 0);

        ArrayList<MinMax> lastMoves = new ArrayList<>();

        Thread[] minMaxThreads = new Thread[firstMinMaxes.size()];

        AtomicInteger index = new AtomicInteger(0);

        firstMinMaxes.forEach(minMax -> {
            minMaxThreads[index.get()] = new Thread(() ->
                    lastMoves.add(minMax.findBestMinMax())
            );
            minMaxThreads[index.get()].setPriority(8);
            minMaxThreads[index.get()].start();
            index.getAndIncrement();
        });

        index.set(0);

        for (int i = 0; i < firstMinMaxes.size(); i++) {
            try {
                minMaxThreads[index.get()].join();
                index.getAndIncrement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return lastMoves.stream()
                .max(Comparator.comparingInt(MinMax::getSumOfMinMax))
                .get();

    }

    private void calculateNextMove() {
        int initialDepth = 3;

        Instant beginning = Instant.now();
        MinMax bestMinMax = findBestMinMax(initialDepth);
        Instant end = Instant.now();

        MinMaxMove bestMove = bestMinMax.getFirstMove();

        System.out.printf("Number of minMaxes in depth of %d search number of %d took %d seconds and got %s move to %s with strength of %d \n",
                initialDepth,
                MinMax.getNumOfMinMaxes(),
                Duration.between(beginning, end).toSeconds(),
                getEnumByArrayPosition(bestMove.getMoveLocation()),
                getEnumByArrayPosition(bestMove.getMoveDestination()),
                bestMinMax.getSumOfMinMax()
        );

        MinMax.setNumOfMinMaxes(0);

        this.moveStart = getEnumByArrayPosition(bestMove.getMoveLocation());
        this.movableChess = Objects.requireNonNull(moveStart).getChess();
        this.moveDestination = getEnumByArrayPosition(bestMove.getMoveDestination());

        do {
            ;
        } while (passedTurnTime.get() < minTurnTime);

        passedTurnTime.set(0.f);
        isReadyToMove.set(true);
        isCalculating.set(false);
    }

    public void calculateMove() {
        isReadyToMove.set(false);
        isCalculating.set(true);
        Thread calculateThread = new Thread(this::calculateNextMove);
        calculateThread.setPriority(9);
        calculateThread.start();
    }

    public String choosePawn() {
        int chosenPawnUpgrade = ThreadLocalRandom.current().nextInt(1, 5);
        String newClazz = "";

        switch (chosenPawnUpgrade) {
            case 1 -> newClazz = "B_BISHOP";
            case 2 -> newClazz = "B_ROOK";
            case 3 -> newClazz = "B_QUEEN";
            case 4 -> newClazz = "B_KNIGHT";
        }

        return newClazz;
    }

    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public Chess getMovableChess() {
        return movableChess;
    }

    public GameBoard.BoardLocations getMoveStart() {
        return moveStart;
    }

    public GameBoard.BoardLocations getMoveDestination() {
        return moveDestination;
    }

    public AtomicBoolean getReadyToMove() {
        return isReadyToMove;
    }

    public AtomicBoolean getIsCalculating() {
        return isCalculating;
    }
}
