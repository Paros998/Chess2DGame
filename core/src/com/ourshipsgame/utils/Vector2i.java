package com.ourshipsgame.utils;

import com.badlogic.gdx.math.Vector2;

public class Vector2i extends Vector2 {
    private int x;
    private int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i() {
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public Vector2i setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Vector2i setY(int y) {
        this.y = y;
        return this;
    }
}
