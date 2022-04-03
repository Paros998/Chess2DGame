package com.ourshipsgame.game;


import com.ourshipsgame.utils.ChessMove;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;


public class GameHistory {
    List<ChessMove> historyList = new ArrayList<ChessMove>();

    Float whiteTimer;
    Float blackTimer;
    Player currentPlayer;


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
            writer.write(currentPlayer.getPlayerName() + "\n");
            writer.write(currentPlayer.getColor() + "\n");
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


    public void historyLoad() {

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
        return currentPlayer;
    }


    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;

    }
}