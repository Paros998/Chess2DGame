package com.ourshipsgame.game;

public class Player {
    public enum PlayerColor{
        WHITE,
        BLACK
    }

    private final PlayerColor color;

    public Player(PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }
}
