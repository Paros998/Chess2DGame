package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.GameObject;
import com.ourshipsgame.game.Player;

public abstract class Chess {
    protected GameObject gameObject;
    protected Player player;
    protected Sound moveSound;
    protected Sound attackSound;

    protected Chess(Texture chessTexture, float posX, float posY){
        gameObject = new GameObject(chessTexture,posX,posY,true,true, new Vector2(1,1));
    }

    //newPos like A7
    protected abstract void moveChess(GameBoard.BoardLocations newPos);

    protected abstract void drawAvailableMovesAndAttacks();

    public GameObject getGameObject() {
        return gameObject;
    }

    public Chess setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public Chess setPlayer(Player player) {
        this.player = player;
        return this;
    }
}
