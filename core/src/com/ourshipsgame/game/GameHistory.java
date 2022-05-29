package com.ourshipsgame.game;

import com.ourshipsgame.utils.ChessMove;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameHistory {
    private final List<ChessMove> historyList = new ArrayList<>();

    private final Player whitePlayer;
    private final Player blackPlayer;
    private Player myPlayer;
    private Player playerTurn;

    //Basic Constructor
    GameHistory(Player myPlayer, Player whitePlayer, Player blackPlayer) {
        this.myPlayer = myPlayer;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.playerTurn = whitePlayer;
    }

    //Load Constructor
    GameHistory(Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public void historySave() {
        File file = new File("gameSave.txt");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();

            }


        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(myPlayer.getColor() + "\n");
            writer.write(myPlayer.getPlayerName() + "\n");
            writer.write(playerTurn.getColor() + "\n");
            writer.write(whitePlayer.getTimeLeft() + "\n");
            writer.write(whitePlayer.getScore() + "\n");
            writer.write(blackPlayer.getTimeLeft() + "\n");
            writer.write(blackPlayer.getScore() + "\n");

            for (ChessMove move : historyList
            ) {
                writer.write(move.write());
                if (historyList.indexOf(move) != historyList.size())
                    writer.write("\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<ChessMove> historyLoad() {
        File file;
        file = new File("gameSave.txt");

        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert scanner != null;
        Player.PlayerColor playerColor = Player.PlayerColor.valueOf(scanner.nextLine());

        if (playerColor.equals(Player.PlayerColor.WHITE))
            myPlayer = whitePlayer;
        else myPlayer = blackPlayer;

        myPlayer.setPlayerName(scanner.nextLine());

        if (myPlayer.equals(whitePlayer))
            blackPlayer.setPlayerName("Bot Clark");
        else whitePlayer.setPlayerName("Bot Clark");

        Player.PlayerColor currentTurnPlayerColor = Player.PlayerColor.valueOf(scanner.nextLine());

        if (currentTurnPlayerColor.equals(Player.PlayerColor.WHITE))
            playerTurn = whitePlayer;
        else playerTurn = blackPlayer;

        whitePlayer.setTimeLeft(Float.parseFloat(scanner.nextLine()));
        whitePlayer.setScore(Integer.parseInt(scanner.nextLine()));

        blackPlayer.setTimeLeft(Float.parseFloat(scanner.nextLine()));
        blackPlayer.setScore(Integer.parseInt(scanner.nextLine()));

        while(scanner.hasNextLine())
            historyList.add(ChessMove.readFromLine(scanner.nextLine()));


        return historyList;
    }


    public void updateHistoryAfterTurn(ChessMove move) {
        historyList.add(move);

        playerTurn = playerTurn == whitePlayer ? blackPlayer : whitePlayer;
    }

    public Player getCurrentPlayer() {
        return myPlayer;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }
}