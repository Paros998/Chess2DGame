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
    protected boolean isDestroyed = false;

    protected Chess(Texture chessTexture, GameBoard.BoardLocations location, AssetManager manager) {
        this.manager = manager;
        gameObject = new GameObject(
                chessTexture,
                location.getPosition().getX(),
                location.getPosition().getY(),
                true,
                false,
                new Vector2(1, 1));
        moveSound = manager.get("core/assets/sounds/move_sound.wav", Sound.class);
        attackSound = manager.get("core/assets/sounds/attack_sound.wav", Sound.class);
        currentLocation = location.setChess(this,true);
        possibleMovesAndAttacksAsVectors = new ArrayList<>();
        possibleMovesVectors = new ArrayList<>();
        possibleAttackVectors = new ArrayList<>();
    }


    public boolean moveChess(GameBoard.BoardLocations newPos, float soundVolume) {
        if (canMove(newPos)) {
            if (newPos.getChess() != null)
                attackSound.play(soundVolume);
            else moveSound.play(soundVolume);

            currentLocation.setChess(null);
            newPos.setChess(this);
            gameObject.setSpritePos(newPos.getPosition());
            currentLocation = newPos;
            return true;
        }
        return false;

    }

    public void moveChessWhileLoading(GameBoard.BoardLocations newPos) {
        currentLocation.setChess(null);
        newPos.setChess(this);
        gameObject.setSpritePos(newPos.getPosition());
        currentLocation = newPos;
    }

    public void drawAvailableMovesAndAttacks(SpriteBatch spriteBatch) {
        for (GameObject move : possibleMovesAndAttacks)
            move.getSprite().draw(spriteBatch, 0.75f);
    }

    public boolean canMove(GameBoard.BoardLocations position) {
        return possibleMovesAndAttacksAsVectors.stream()
                .anyMatch(vector2i -> {
                    if (position.getChess() == null)
                        return vector2i.epsilonEquals(position.getArrayPosition());
                    return vector2i.epsilonEquals(position.getArrayPosition()) && !position.getChess().getClass().equals(King.class);
                });
    }

    public GameObject[] getPossibleMovesAndAttacks() {
        return possibleMovesAndAttacks;
    }

    public ArrayList<Vector2i> getPossibleMovesVectors() {
        return possibleMovesVectors;
    }

    public boolean clickedOnThisChess(int xPos, int yPos, GameBoard gameBoard) {
        if (!isDestroyed)
            return gameObject.spriteContains(new Vector2(xPos, yPos));
        return false;
//
//        if (gameObject.spriteContains(new Vector2(xPos, yPos))) {
//            //evaluateMoves(gameBoard);
//            return true;
//        }
//        return false;
    }

    public void evaluateMoves(GameBoard gameBoard) {
        if (!isDestroyed) {
            possibleMovesAndAttacksAsVectors.clear();
            possibleMovesVectors.clear();
            possibleAttackVectors.clear();
            calculatePossibleMoves(gameBoard);
            filterMoves(gameBoard);
            createMovesObjects(gameBoard);
        }
    }

    protected void createMovesObjects(GameBoard gameBoard) {
        int possibleMoves = possibleMovesAndAttacksAsVectors.size();
        possibleMovesAndAttacks = new GameObject[possibleMoves];
        String texturePath;

        for (int i = 0; i < possibleMoves; i++) {
            Vector2i move = possibleMovesAndAttacksAsVectors.get(i);
            GameBoard.BoardLocations location = gameBoard.getBoard()[move.getX()][move.getY()];

            texturePath = location.getChess() == null ? "core/assets/moves/move.png" : "core/assets/moves/attack.png";

            possibleMovesAndAttacks[i] = new GameObject(
                    manager.get(texturePath, Texture.class),
                    location.getPosition().getX(),
                    location.getPosition().getY(),
                    true,
                    false,
                    null
            );
        }
    }

    protected void filterMoves(GameBoard gameBoard) {
        GameBoard.BoardLocations[][] board = gameBoard.getBoard();

        Predicate<Vector2i> movePredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() == null;

        Predicate<Vector2i> attackPredicate = vector2i ->
                board[vector2i.getX()][vector2i.getY()].getChess() != null &&
                        board[vector2i.getX()][vector2i.getY()].getChess().getPlayer() != player;

        Predicate<Vector2i> xOffsetPredicate = vector2i -> vector2i.getX() >= 0 && vector2i.getX() <= 7;
        Predicate<Vector2i> yOffsetPredicate = vector2i -> vector2i.getY() >= 0 && vector2i.getY() <= 7;

        Predicate<Vector2i> vector2iPredicate = xOffsetPredicate.and(yOffsetPredicate);

        possibleMovesVectors = (ArrayList<Vector2i>) possibleMovesVectors.stream()
                .filter(vector2iPredicate.and(movePredicate))
                .collect(Collectors.toList());

        possibleAttackVectors = (ArrayList<Vector2i>) possibleAttackVectors.stream()
                .filter(vector2iPredicate.and(attackPredicate))
                .collect(Collectors.toList());

        possibleMovesAndAttacksAsVectors.addAll(possibleMovesVectors);
        possibleMovesAndAttacksAsVectors.addAll(possibleAttackVectors);
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameBoard.BoardLocations getCurrentLocation() {
        return currentLocation;
    }

    public ArrayList<Vector2i> getPossibleAttackVectors() {
        return possibleAttackVectors;
    }

    public ArrayList<Vector2i> getPossibleMovesAndAttacksAsVectors() {
        return possibleMovesAndAttacksAsVectors;
    }

    public abstract Integer getStrength();

    protected abstract void calculatePossibleMoves(GameBoard gameBoard);

    protected static boolean checkIfNotCrossedWithChessHorizontally(Vector2i vector2i, GameBoard gameBoard, GameBoard.BoardLocations currentLocation) {
        int currentX = currentLocation.getArrayPosition().getX();
        boolean checkRight = vector2i.getX() > currentX;

        if (currentLocation.getArrayPosition().getY() != vector2i.getY())
            return true;

        if (checkRight) {
            int difference = vector2i.getX() - currentX;
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[currentX + i][vector2i.getY()].getChess() != null)
                    return false;

        } else {
            int difference = currentX - vector2i.getX();
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[currentX - i][vector2i.getY()].getChess() != null)
                    return false;

        }
        return true;
    }

    protected static boolean checkIfNotCrossedWithChessVertically(Vector2i vector2i, GameBoard gameBoard, GameBoard.BoardLocations currentLocation) {
        int currentY = currentLocation.getArrayPosition().getY();
        boolean checkTop = vector2i.getY() > currentY;

        if (currentLocation.getArrayPosition().getX() != vector2i.getX())
            return true;

        if (checkTop) {
            int difference = vector2i.getY() - currentY;
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[vector2i.getX()][currentY + i].getChess() != null)
                    return false;

        } else {
            int difference = currentY - vector2i.getY();
            for (int i = 1; i < difference; i++)
                if (gameBoard.getBoard()[vector2i.getX()][currentY - i].getChess() != null)
                    return false;

        }
        return true;
    }

    protected static boolean checkIfNotCrossedWithChessDiagonally(Vector2i vector2i, GameBoard gameBoard, GameBoard.BoardLocations currentLocation) {
        int currentX = currentLocation.getArrayPosition().getX();
        int currentY = currentLocation.getArrayPosition().getY();

        if (currentX == vector2i.getX())
            return true;

        if (currentY == vector2i.getY())
            return true;

        boolean checkRight = vector2i.getX() > currentX;
        boolean checkTop = vector2i.getY() > currentY;

        int differenceX;

        if (checkRight) {
            differenceX = vector2i.getX() - currentX;

            if (checkTop) {
                for (int i = 1; i < differenceX; i++) {
                    if (currentX + i > 7 || currentY + i > 7)
                        break;
                    if (gameBoard.getBoard()[currentX + i][currentY + i].getChess() != null)
                        return false;
                }

            } else
                for (int i = 1; i < differenceX; i++) {
                    if (currentX + i > 7 || currentY - i < 0)
                        break;
                    if (gameBoard.getBoard()[currentX + i][currentY - i].getChess() != null)
                        return false;
                }

        } else {
            differenceX = currentX - vector2i.getX();

            if (checkTop) {
                for (int i = 1; i < differenceX; i++) {
                    if (currentX - i < 0 || currentY + i > 7)
                        break;
                    if (gameBoard.getBoard()[currentX - i][currentY + i].getChess() != null)
                        return false;
                }
            } else
                for (int i = 1; i < differenceX; i++) {
                    if (currentX - i < 0 || currentY - i < 0)
                        break;
                    if (gameBoard.getBoard()[currentX - i][currentY - i].getChess() != null)
                        return false;
                }

        }

        return true;
    }
}
