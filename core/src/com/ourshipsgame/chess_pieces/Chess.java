package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ourshipsgame.game.GameObject;

public abstract class Chess {
    protected GameObject gameObject;
    protected int playerAssigned;

    protected Chess(Texture chessTexture, float posX, float posY, int playerAssigned){
        gameObject = new GameObject(chessTexture,posX,posY,true,true, new Vector2(1,1));
        this.playerAssigned = playerAssigned;
    };

    //newPos like A7
    protected abstract void moveChess(String newPos);

    protected abstract void drawAvailableMovesAndAttacks();
}
