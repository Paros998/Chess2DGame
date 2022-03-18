package com.ourshipsgame.game;

import com.badlogic.gdx.math.Vector2;
import com.ourshipsgame.chess_pieces.Chess;
import com.ourshipsgame.utils.Vector2i;
import org.lwjgl.util.vector.Vector2f;

import java.util.Iterator;
import java.util.List;

import static com.ourshipsgame.handlers.Constant.*;

public class GameBoard {
    protected GameObject gameBoardObject;

    final static float boardXOffset = 50.f;
    final static float boardYOffset = 51.f;

    public enum BoardLocations {
        A1(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset),
                new Vector2i(0, 0)),
        A2(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset + BOX_WIDTH_F),
                new Vector2i(0, 1)),
        A3(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset + (2 * BOX_WIDTH_F)),
                new Vector2i(0, 2)),
        A4(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset + (3 * BOX_WIDTH_F)),
                new Vector2i(0, 3)),
        A5(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset + (4 * BOX_WIDTH_F)),
                new Vector2i(0, 4)),
        A6(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset + (5 * BOX_WIDTH_F)),
                new Vector2i(0, 5)),
        A7(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset + (6 * BOX_WIDTH_F)),
                new Vector2i(0, 6)),
        A8(new Vector2f(X_AXIS_BOARD_START + boardXOffset, Y_AXIS_BOARD_START + boardYOffset + (7 * BOX_WIDTH_F)),
                new Vector2i(0, 7)),

        B1(new Vector2f(A1.position.x + BOX_WIDTH_F, A1.position.y),
                new Vector2i(1,0)),
        B2(new Vector2f(A2.position.x + BOX_WIDTH_F, A2.position.y),
                new Vector2i(1,1)),
        B3(new Vector2f(A3.position.x + BOX_WIDTH_F, A3.position.y),
                new Vector2i(1,2)),
        B4(new Vector2f(A4.position.x + BOX_WIDTH_F, A4.position.y),
                new Vector2i(1,3)),
        B5(new Vector2f(A5.position.x + BOX_WIDTH_F, A5.position.y),
                new Vector2i(1,4)),
        B6(new Vector2f(A6.position.x + BOX_WIDTH_F, A6.position.y),
                new Vector2i(1,5)),
        B7(new Vector2f(A7.position.x + BOX_WIDTH_F, A7.position.y),
                new Vector2i(1,6)),
        B8(new Vector2f(A8.position.x + BOX_WIDTH_F, A8.position.y),
                new Vector2i(1,7)),

        C1(new Vector2f(B1.position.x + BOX_WIDTH_F, B1.position.y),
                new Vector2i(2,0)),
        C2(new Vector2f(B2.position.x + BOX_WIDTH_F, B2.position.y),
                new Vector2i(2,1)),
        C3(new Vector2f(B3.position.x + BOX_WIDTH_F, B3.position.y),
                new Vector2i(2,2)),
        C4(new Vector2f(B4.position.x + BOX_WIDTH_F, B4.position.y),
                new Vector2i(2,3)),
        C5(new Vector2f(B5.position.x + BOX_WIDTH_F, B5.position.y),
                new Vector2i(2,4)),
        C6(new Vector2f(B6.position.x + BOX_WIDTH_F, B6.position.y),
                new Vector2i(2,5)),
        C7(new Vector2f(B7.position.x + BOX_WIDTH_F, B7.position.y),
                new Vector2i(2,6)),
        C8(new Vector2f(B8.position.x + BOX_WIDTH_F, B8.position.y),
                new Vector2i(2,7)),

        D1(new Vector2f(C1.position.x + BOX_WIDTH_F, C1.position.y),
                new Vector2i(3,0)),
        D2(new Vector2f(C2.position.x + BOX_WIDTH_F, C2.position.y),
                new Vector2i(3,1)),
        D3(new Vector2f(C3.position.x + BOX_WIDTH_F, C3.position.y),
                new Vector2i(3,2)),
        D4(new Vector2f(C4.position.x + BOX_WIDTH_F, C4.position.y),
                new Vector2i(3,3)),
        D5(new Vector2f(C5.position.x + BOX_WIDTH_F, C5.position.y),
                new Vector2i(3,4)),
        D6(new Vector2f(C6.position.x + BOX_WIDTH_F, C6.position.y),
                new Vector2i(3,5)),
        D7(new Vector2f(C7.position.x + BOX_WIDTH_F, C7.position.y),
                new Vector2i(3,6)),
        D8(new Vector2f(C8.position.x + BOX_WIDTH_F, C8.position.y),
                new Vector2i(3,7)),

        E1(new Vector2f(D1.position.x + BOX_WIDTH_F, D1.position.y),
                new Vector2i(4,0)),
        E2(new Vector2f(D2.position.x + BOX_WIDTH_F, D2.position.y),
                new Vector2i(4,1)),
        E3(new Vector2f(D3.position.x + BOX_WIDTH_F, D3.position.y),
                new Vector2i(4,2)),
        E4(new Vector2f(D4.position.x + BOX_WIDTH_F, D4.position.y),
                new Vector2i(4,3)),
        E5(new Vector2f(D5.position.x + BOX_WIDTH_F, D5.position.y),
                new Vector2i(4,4)),
        E6(new Vector2f(D6.position.x + BOX_WIDTH_F, D6.position.y),
                new Vector2i(4,5)),
        E7(new Vector2f(D7.position.x + BOX_WIDTH_F, D7.position.y),
                new Vector2i(4,6)),
        E8(new Vector2f(D8.position.x + BOX_WIDTH_F, D8.position.y),
                new Vector2i(4,7)),

        F1(new Vector2f(E1.position.x + BOX_WIDTH_F, E1.position.y),
                new Vector2i(5,0)),
        F2(new Vector2f(E2.position.x + BOX_WIDTH_F, E2.position.y),
                new Vector2i(5,1)),
        F3(new Vector2f(E3.position.x + BOX_WIDTH_F, E3.position.y),
                new Vector2i(5,2)),
        F4(new Vector2f(E4.position.x + BOX_WIDTH_F, E4.position.y),
                new Vector2i(5,3)),
        F5(new Vector2f(E5.position.x + BOX_WIDTH_F, E5.position.y),
                new Vector2i(5,4)),
        F6(new Vector2f(E6.position.x + BOX_WIDTH_F, E6.position.y),
                new Vector2i(5,5)),
        F7(new Vector2f(E7.position.x + BOX_WIDTH_F, E7.position.y),
                new Vector2i(5,6)),
        F8(new Vector2f(E8.position.x + BOX_WIDTH_F, E8.position.y),
                new Vector2i(5,7)),

        G1(new Vector2f(F1.position.x + BOX_WIDTH_F, F1.position.y),
                new Vector2i(6,0)),
        G2(new Vector2f(F2.position.x + BOX_WIDTH_F, F2.position.y),
                new Vector2i(6,1)),
        G3(new Vector2f(F3.position.x + BOX_WIDTH_F, F3.position.y),
                new Vector2i(6,2)),
        G4(new Vector2f(F4.position.x + BOX_WIDTH_F, F4.position.y),
                new Vector2i(6,3)),
        G5(new Vector2f(F5.position.x + BOX_WIDTH_F, F5.position.y),
                new Vector2i(6,4)),
        G6(new Vector2f(F6.position.x + BOX_WIDTH_F, F6.position.y),
                new Vector2i(6,5)),
        G7(new Vector2f(F7.position.x + BOX_WIDTH_F, F7.position.y),
                new Vector2i(6,6)),
        G8(new Vector2f(F8.position.x + BOX_WIDTH_F, F8.position.y),
                new Vector2i(6,7)),

        H1(new Vector2f(G1.position.x + BOX_WIDTH_F, G1.position.y),
                new Vector2i(7,0)),
        H2(new Vector2f(G2.position.x + BOX_WIDTH_F, G2.position.y),
                new Vector2i(7,1)),
        H3(new Vector2f(G3.position.x + BOX_WIDTH_F, G3.position.y),
                new Vector2i(7,2)),
        H4(new Vector2f(G4.position.x + BOX_WIDTH_F, G4.position.y),
                new Vector2i(7,3)),
        H5(new Vector2f(G5.position.x + BOX_WIDTH_F, G5.position.y),
                new Vector2i(7,4)),
        H6(new Vector2f(G6.position.x + BOX_WIDTH_F, G6.position.y),
                new Vector2i(7,5)),
        H7(new Vector2f(G7.position.x + BOX_WIDTH_F, G7.position.y),
                new Vector2i(7,6)),
        H8(new Vector2f(G8.position.x + BOX_WIDTH_F, G8.position.y),
                new Vector2i(7,7)),
        ;

        private final Vector2f position;

        public Vector2i getArrayPosition() {
            return arrayPosition;
        }

        private final Vector2i arrayPosition;
        private Chess chess;

        BoardLocations(Vector2f position, Vector2i arrayPosition) {
            this.position = position;
            this.arrayPosition = arrayPosition;
        }

        public Vector2f getPosition() {
            return position;
        }

        public Chess getChess() {
            return chess;
        }

        public BoardLocations setChess(Chess chess) {
            if(this.chess != null)
                this.chess.getGameObject().setSpritePos(new Vector2(-200,-200));

            this.chess = chess;
            return this;
        }
    }

    public BoardLocations[][] getBoard() {
        return board;
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
