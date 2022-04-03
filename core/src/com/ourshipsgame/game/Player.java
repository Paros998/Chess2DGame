package com.ourshipsgame.game;

public class Player {

    public enum PlayerColor {
        WHITE,
        BLACK
    }

    private Float timeLeft;
    private Integer score;
    private String playerName;
    private final PlayerColor color;

    public Player(PlayerColor color) {
        this.color = color;
        this.timeLeft = 3600.f;
        score = 0;
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

    public void addScore(Integer addition) {
        score += addition;
    }

    public Integer getScore() {
        return score;
    }
}
