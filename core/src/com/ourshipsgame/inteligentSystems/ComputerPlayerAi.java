package com.ourshipsgame.inteligentSystems;

import com.ourshipsgame.chess_pieces.Chess;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.Player;
import org.lwjgl.util.vector.Vector2f;

import java.util.concurrent.ThreadLocalRandom;

import static com.ourshipsgame.game.GameBoard.BoardLocations.getEnumByPosition;

/**
 * Klasa odpowiadająca za podejmowanie decyzji przez komputer
 */
public class ComputerPlayerAi {

    private Player player;
    private final Chess[] myCheeses;
    private Chess movableChess;
    private GameBoard.BoardLocations moveStart;
    private GameBoard.BoardLocations moveDestination;
    private Float passedTurnTime = 0.f;
    private Boolean isReadyToMove = false;

    /**
     * Konstruktor główny obiektu, który inicjuje i ustawia dane do obliczeń logiki
     *
     * @param myCheeses array of AI cheeses

     */
    public ComputerPlayerAi(Chess[] myCheeses) {

        this.myCheeses = myCheeses;
    }

    public void update(float rawTime){
        float requiredPassTime = 1.5f;

        passedTurnTime += rawTime;

        if(passedTurnTime >= requiredPassTime){
            passedTurnTime = 0.f;
            isReadyToMove = true;
        }
    }

    public void calculateMove() {
        int chosenChess = ThreadLocalRandom.current().nextInt(0, 16);

        while (myCheeses[chosenChess].isDestroyed() || myCheeses[chosenChess].getPossibleMovesAndAttacks().length == 0)
            chosenChess = ThreadLocalRandom.current().nextInt(0, 16);

        Chess movableChess = myCheeses[chosenChess];

        int numberOfAvailableMoves = movableChess.getPossibleMovesAndAttacks().length;

        int move = ThreadLocalRandom.current().nextInt(0, numberOfAvailableMoves);

        GameBoard.BoardLocations newPosition =
                getEnumByPosition(movableChess.getPossibleMovesAndAttacks()[move].getPosition());

        while (!movableChess.canMove(newPosition)) {
            move = ThreadLocalRandom.current().nextInt(0, numberOfAvailableMoves);
            newPosition = getEnumByPosition(movableChess.getPossibleMovesAndAttacks()[move].getPosition());
        }

        this.movableChess = movableChess;
        this.moveStart = movableChess.getCurrentLocation();
        this.moveDestination = newPosition;
        this.isReadyToMove = false;
    }

    public String choosePawn() {
        int chosenPawnUpgrade = ThreadLocalRandom.current().nextInt(1, 5);
        String newClazz = "";

        switch (chosenPawnUpgrade){
            case 1 -> newClazz = "B_BISHOP";
            case 2 -> newClazz = "B_ROOK";
            case 3 -> newClazz = "B_QUEEN";
            case 4 -> newClazz = "B_KNIGHT";
        }

        return newClazz;
    }

    public void setPlayer(Player myPlayer) {
        this.player = myPlayer;
    }

    public Player getPlayer() {
        return player;
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

    public Boolean getReadyToMove() {
        return isReadyToMove;
    }
}
