package com.ourshipsgame.utils;

public class MinMaxMove {
    final Vector2i moveLocation;
    final Vector2i moveDestination;

    public MinMaxMove(Vector2i moveLocation, Vector2i moveDestination) {
        this.moveLocation = moveLocation;
        this.moveDestination = moveDestination;
    }

    public Vector2i getMoveLocation() {
        return moveLocation;
    }

    public Vector2i getMoveDestination() {
        return moveDestination;
    }
}
