package com.ourshipsgame.game;

public class Player {
    public enum PlayerColor{
        WHITE,
        BLACK
    }

    private final Float timeLeft;

    private final PlayerColor color;

    public Player(PlayerColor color) {
        this.color = color;
        this.timeLeft = 3600.f;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Float getTimeLeft(){return timeLeft;}
}
