package com.ourshipsgame.game;

import com.ourshipsgame.chess_pieces.Chess;

public class Player {

    public enum PlayerColor {
        WHITE,
        BLACK
    }

    private Float timeLeft;
    private Integer score;
    private String playerName;
    private final PlayerColor color;
    private Chess[] myCheeses;
    private boolean madeMoveSinceKingIsChecked = false;

    public Player(PlayerColor color, Chess[] myCheeses) {
        this.color = color;
        this.myCheeses = myCheeses;
        this.timeLeft = 3600.f;
        score = 0;
    }

    public Player(PlayerColor color) {
        this.color = color;
        this.timeLeft = 3600.f;
        score = 0;
    }

    public Chess[] getMyCheeses() {
        return myCheeses;
    }

    public boolean isMadeMoveSinceKingIsChecked() {
        return madeMoveSinceKingIsChecked;
    }

    public void setMadeMoveSinceKingIsChecked(boolean madeMoveSinceKingIsChecked) {
        this.madeMoveSinceKingIsChecked = madeMoveSinceKingIsChecked;
    }

    public void updateTime(float deltaTime) {
        timeLeft -= deltaTime;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Float getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Float timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void addScore(Integer addition) {
        score += addition;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
