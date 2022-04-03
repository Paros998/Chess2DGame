package com.ourshipsgame.game;


import com.ourshipsgame.utils.ChessMove;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GameHistory {
    List<ChessMove> historyList = new ArrayList<ChessMove>();

    Float whiteTimer;
    Float blackTimer;
    Player myPlayer;
    Player playerTurn;


    public void historySave() {
        File file = new File("gameSave.txt");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();

            }
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(myPlayer.getColor() + "\n");
            writer.write(myPlayer.getPlayerName() + "\n");
            writer.write(playerTurn.getColor() + "\n");
            writer.write(whiteTimer + "\n");
            writer.write(blackTimer + "\n");

            for (ChessMove move : historyList
            ) {
                writer.write(move.write() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void historyLoad(Player whitePlayer, Player blackPlayer, SinglePlayerGameScreen gameScreen) {

        Scanner scanner = new Scanner("gameSave.txt");

        Player.PlayerColor playerColor = Player.PlayerColor.valueOf(scanner.nextLine());

        if(playerColor.equals(Player.PlayerColor.WHITE))
            myPlayer = whitePlayer;
        else myPlayer = blackPlayer;

        myPlayer.setPlayerName(scanner.nextLine());


        Player.PlayerColor currentTurnPlayerColor = Player.PlayerColor.valueOf(scanner.nextLine());

        if(currentTurnPlayerColor.equals(Player.PlayerColor.WHITE))
            playerTurn = whitePlayer;
        else playerTurn = blackPlayer;

        whitePlayer.setTimeLeft(Float.parseFloat(scanner.nextLine()));

        blackPlayer.setTimeLeft(Float.parseFloat(scanner.nextLine()));

        //TODO create loading with game chesses changes
    }


    public void appendMove(ChessMove move) {
        historyList.add(move);
    }


    public Float getWhiteTimer() {
        return whiteTimer;
    }


    public void setWhiteTimer(Float whiteTimer) {
        this.whiteTimer = whiteTimer;
    }


    public Float getBlackTimer() {
        return blackTimer;
    }


    public void setBlackTimer(Float blackTimer) {
        this.blackTimer = blackTimer;
    }

    public Player getCurrentPlayer() {
        return myPlayer;
    }


    public void setCurrentPlayer(Player currentPlayer) {
        this.myPlayer = currentPlayer;

    }
}