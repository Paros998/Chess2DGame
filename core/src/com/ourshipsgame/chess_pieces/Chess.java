package com.ourshipsgame.chess_pieces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ourshipsgame.game.GameBoard;
import com.ourshipsgame.game.GameObject;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.utils.Vector2i;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Chess {
    protected GameObject gameObject;
    protected Player player;
    protected Sound moveSound;
    protected Sound attackSound;
    protected GameBoard.BoardLocations currentLocation;
    protected GameObject[] possibleMovesAndAttacks;
    protected ArrayList<Vector2i> possibleMovesAndAttacksAsVectors;
    protected ArrayList<Vector2i> possibleMovesVectors;
    protected ArrayList<Vector2i> possibleAttackVectors;
    protected AssetManager manager;

    protected Chess(Texture chessTexture, GameBoard.BoardLocations location,AssetManager manager){
        this.manager = manager;
        gameObject = new GameObject(
                chessTexture,
                location.getPosition().getX(),
                location.getPosition().getY(),
                true,
                false,
                new Vector2(1,1));
        location.setChess(this);
        currentLocation = location;
        possibleMovesAndAttacksAsVectors = new ArrayList<>();
        possibleMovesVectors = new ArrayList<>();
        possibleAttackVectors = new ArrayList<>();
    }

    //newPos like A7
    public void moveChess(GameBoard.BoardLocations newPos){
        if(canMove(newPos)){
            currentLocation.setChess(null);
            newPos.setChess(this);
            gameObject.setSpritePos(newPos.getArrayPosition());
        }
    }

    public void drawAvailableMovesAndAttacks(SpriteBatch spriteBatch,GameBoard gameBoard){
        for (GameObject move: possibleMovesAndAttacks)
            move.drawSprite(spriteBatch);
    }

    private boolean canMove(GameBoard.BoardLocations position){
        return possibleMovesAndAttacksAsVectors.stream()
                .anyMatch(vector2i -> vector2i.epsilonEquals(position.getArrayPosition()));
    }

    public boolean clickedOnThisChess(int xPos,int yPos,GameBoard gameBoard){
        if(gameObject.spriteContains(new Vector2(xPos,yPos))){
            possibleMovesAndAttacksAsVectors.clear();
            possibleMovesVectors.clear();
            possibleAttackVectors.clear();
            calculatePossibleMoves(gameBoard);
            filterMoves(gameBoard);
            createMovesObjects(gameBoard);
            return true;
        }
        return false;
    }

    private void createMovesObjects(GameBoard gameBoard) {
        int possibleMoves = possibleMovesAndAttacksAsVectors.size();
        possibleMovesAndAttacks = new GameObject[possibleMoves];
        String texturePath;

        for (int i = 0; i < possibleMoves; i++) {
            Vector2i move = possibleMovesAndAttacksAsVectors.get(i);
            GameBoard.BoardLocations location = gameBoard.getBoard()[move.getX()][move.getY()];

            texturePath = location.getChess() == null ? "core/assets/moves/move.png" : "core/assets/moves/attack.png";

            possibleMovesAndAttacks[i] = new GameObject(
                    manager.get(texturePath,Texture.class),
                    location.getPosition().getX(),
                    location.getPosition().getY(),
                    true,
                    false,
                    null
            );
        }
    }

    protected void filterMoves(GameBoard gameBoard){
        GameBoard.BoardLocations[][] board = gameBoard.getBoard();

        Predicate<Vector2i> movePredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() == null;

        Predicate<Vector2i> attackPredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() != null &&
                        board[vector2i.getX()][vector2i.getY()].getChess().getPlayer() != player;

        Predicate<Vector2i> xOffsetPredicate = vector2i -> vector2i.getX() >= 0 && vector2i.getX() <= 7;
        Predicate<Vector2i> yOffsetPredicate = vector2i -> vector2i.getY() >= 0 && vector2i.getY() <= 7;

        possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                .filter(xOffsetPredicate)
                .filter(yOffsetPredicate)
                .filter(movePredicate)
                .collect(Collectors.toList());

        possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                .filter(xOffsetPredicate)
                .filter(yOffsetPredicate)
                .filter(attackPredicate)
                .collect(Collectors.toList());

        possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);
        possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
    }


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

    protected abstract void calculatePossibleMoves(GameBoard gameBoard);
}
