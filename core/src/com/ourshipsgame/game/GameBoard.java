package com.ourshipsgame.game;

import static com.ourshipsgame.game.GameBoard.BoardLocations.*;
import static com.ourshipsgame.handlers.Constant.*;

import com.ourshipsgame.chess_pieces.Chess;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class GameBoard {
    protected GameObject gameBoardObject;

    final static float boardXOffset = 50.f;
    final static float boardYOffset = 51.f;

    public enum BoardLocations {
        A1(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset)),
        A2(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset + BOX_WIDTH_F)),
        A3(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset + (2 * BOX_WIDTH_F))),
        A4(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset + (3 * BOX_WIDTH_F))),
        A5(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset + (4 * BOX_WIDTH_F))),
        A6(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset + (5 * BOX_WIDTH_F))),
        A7(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset + (6 * BOX_WIDTH_F))),
        A8(new Vector2f(X_AXIS_BOARD_START + boardXOffset,Y_AXIS_BOARD_START + boardYOffset + (7 * BOX_WIDTH_F))),

        B1(new Vector2f(A1.position.x + BOX_WIDTH_F,A1.position.y)),
        B2(new Vector2f(A2.position.x + BOX_WIDTH_F,A2.position.y)),
        B3(new Vector2f(A3.position.x + BOX_WIDTH_F,A3.position.y)),
        B4(new Vector2f(A4.position.x + BOX_WIDTH_F,A4.position.y)),
        B5(new Vector2f(A5.position.x + BOX_WIDTH_F,A5.position.y)),
        B6(new Vector2f(A6.position.x + BOX_WIDTH_F,A6.position.y)),
        B7(new Vector2f(A7.position.x + BOX_WIDTH_F,A7.position.y)),
        B8(new Vector2f(A8.position.x + BOX_WIDTH_F,A8.position.y)),

        C1(new Vector2f(B1.position.x + BOX_WIDTH_F,B1.position.y)),
        C2(new Vector2f(B2.position.x + BOX_WIDTH_F,B2.position.y)),
        C3(new Vector2f(B3.position.x + BOX_WIDTH_F,B3.position.y)),
        C4(new Vector2f(B4.position.x + BOX_WIDTH_F,B4.position.y)),
        C5(new Vector2f(B5.position.x + BOX_WIDTH_F,B5.position.y)),
        C6(new Vector2f(B6.position.x + BOX_WIDTH_F,B6.position.y)),
        C7(new Vector2f(B7.position.x + BOX_WIDTH_F,B7.position.y)),
        C8(new Vector2f(B8.position.x + BOX_WIDTH_F,B8.position.y)),

        D1(new Vector2f(C1.position.x + BOX_WIDTH_F,C1.position.y)),
        D2(new Vector2f(C2.position.x + BOX_WIDTH_F,C2.position.y)),
        D3(new Vector2f(C3.position.x + BOX_WIDTH_F,C3.position.y)),
        D4(new Vector2f(C4.position.x + BOX_WIDTH_F,C4.position.y)),
        D5(new Vector2f(C5.position.x + BOX_WIDTH_F,C5.position.y)),
        D6(new Vector2f(C6.position.x + BOX_WIDTH_F,C6.position.y)),
        D7(new Vector2f(C7.position.x + BOX_WIDTH_F,C7.position.y)),
        D8(new Vector2f(C8.position.x + BOX_WIDTH_F,C8.position.y)),

        E1(new Vector2f(D1.position.x + BOX_WIDTH_F,D1.position.y)),
        E2(new Vector2f(D2.position.x + BOX_WIDTH_F,D2.position.y)),
        E3(new Vector2f(D3.position.x + BOX_WIDTH_F,D3.position.y)),
        E4(new Vector2f(D4.position.x + BOX_WIDTH_F,D4.position.y)),
        E5(new Vector2f(D5.position.x + BOX_WIDTH_F,D5.position.y)),
        E6(new Vector2f(D6.position.x + BOX_WIDTH_F,D6.position.y)),
        E7(new Vector2f(D7.position.x + BOX_WIDTH_F,D7.position.y)),
        E8(new Vector2f(D8.position.x + BOX_WIDTH_F,D8.position.y)),

        F1(new Vector2f(E1.position.x + BOX_WIDTH_F,E1.position.y)),
        F2(new Vector2f(E2.position.x + BOX_WIDTH_F,E2.position.y)),
        F3(new Vector2f(E3.position.x + BOX_WIDTH_F,E3.position.y)),
        F4(new Vector2f(E4.position.x + BOX_WIDTH_F,E4.position.y)),
        F5(new Vector2f(E5.position.x + BOX_WIDTH_F,E5.position.y)),
        F6(new Vector2f(E6.position.x + BOX_WIDTH_F,E6.position.y)),
        F7(new Vector2f(E7.position.x + BOX_WIDTH_F,E7.position.y)),
        F8(new Vector2f(E8.position.x + BOX_WIDTH_F,E8.position.y)),

        G1(new Vector2f(F1.position.x + BOX_WIDTH_F,F1.position.y)),
        G2(new Vector2f(F2.position.x + BOX_WIDTH_F,F2.position.y)),
        G3(new Vector2f(F3.position.x + BOX_WIDTH_F,F3.position.y)),
        G4(new Vector2f(F4.position.x + BOX_WIDTH_F,F4.position.y)),
        G5(new Vector2f(F5.position.x + BOX_WIDTH_F,F5.position.y)),
        G6(new Vector2f(F6.position.x + BOX_WIDTH_F,F6.position.y)),
        G7(new Vector2f(F7.position.x + BOX_WIDTH_F,F7.position.y)),
        G8(new Vector2f(F8.position.x + BOX_WIDTH_F,F8.position.y)),

        H1(new Vector2f(G1.position.x + BOX_WIDTH_F,G1.position.y)),
        H2(new Vector2f(G2.position.x + BOX_WIDTH_F,G2.position.y)),
        H3(new Vector2f(G3.position.x + BOX_WIDTH_F,G3.position.y)),
        H4(new Vector2f(G4.position.x + BOX_WIDTH_F,G4.position.y)),
        H5(new Vector2f(G5.position.x + BOX_WIDTH_F,G5.position.y)),
        H6(new Vector2f(G6.position.x + BOX_WIDTH_F,G6.position.y)),
        H7(new Vector2f(G7.position.x + BOX_WIDTH_F,G7.position.y)),
        H8(new Vector2f(G8.position.x + BOX_WIDTH_F,G8.position.y)),
        ;

        private final Vector2f position;
        private Chess chess;

        BoardLocations(Vector2f position) {
            this.position = position;
        }

        public Vector2f getPosition() {
            return position;
        }

        public Chess getChess() {
            return chess;
        }

        public BoardLocations setChess(Chess chess) {
            this.chess = chess;
            return this;
        }
    }

    protected BoardLocations[][] board;

    public GameBoard() {
        board = new BoardLocations[8][8];

        List<BoardLocations> boardLocationsList = List.of(BoardLocations.values());
        Iterator<BoardLocations> boardLocationsIterator = boardLocationsList.iterator();

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                board[i][j] = boardLocationsIterator.next();
            }

    }
}
