package com.ourshipsgame.game;

import com.ourshipsgame.chess_pieces.Chess;
import static com.ourshipsgame.handlers.Constant.*;
import org.lwjgl.util.vector.Vector2f;

public class GameBoard {
    protected GameObject Board;

    public enum BoardLocations {
        A1(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f)),
        A2(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f + 120.f)),
        A3(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f + 240.f)),
        A4(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f + 360.f)),
        A5(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f + 480.f)),
        A6(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f + 600.f)),
        A7(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f + 720.f)),
        A8(new Vector2f(X_AXIS_BOARD_START + 60.f,Y_AXIS_BOARD_START + 60.f + 840.f)),

        B1(new Vector2f(A1.position.translate(120.f,0.f))),
        B2(new Vector2f(A2.position.translate(120.f,0.f))),
        B3(new Vector2f(A3.position.translate(120.f,0.f))),
        B4(new Vector2f(A4.position.translate(120.f,0.f))),
        B5(new Vector2f(A5.position.translate(120.f,0.f))),
        B6(new Vector2f(A6.position.translate(120.f,0.f))),
        B7(new Vector2f(A7.position.translate(120.f,0.f))),
        B8(new Vector2f(A8.position.translate(120.f,0.f))),

        C1(new Vector2f(B1.position.translate(120.f,0.f))),
        C2(new Vector2f(B2.position.translate(120.f,0.f))),
        C3(new Vector2f(B3.position.translate(120.f,0.f))),
        C4(new Vector2f(B4.position.translate(120.f,0.f))),
        C5(new Vector2f(B5.position.translate(120.f,0.f))),
        C6(new Vector2f(B6.position.translate(120.f,0.f))),
        C7(new Vector2f(B7.position.translate(120.f,0.f))),
        C8(new Vector2f(B8.position.translate(120.f,0.f))),

        D1(new Vector2f(C1.position.translate(120.f,0.f))),
        D2(new Vector2f(C2.position.translate(120.f,0.f))),
        D3(new Vector2f(C3.position.translate(120.f,0.f))),
        D4(new Vector2f(C4.position.translate(120.f,0.f))),
        D5(new Vector2f(C5.position.translate(120.f,0.f))),
        D6(new Vector2f(C6.position.translate(120.f,0.f))),
        D7(new Vector2f(C7.position.translate(120.f,0.f))),
        D8(new Vector2f(C8.position.translate(120.f,0.f))),

        E1(new Vector2f(D1.position.translate(120.f,0.f))),
        E2(new Vector2f(D2.position.translate(120.f,0.f))),
        E3(new Vector2f(D3.position.translate(120.f,0.f))),
        E4(new Vector2f(D4.position.translate(120.f,0.f))),
        E5(new Vector2f(D5.position.translate(120.f,0.f))),
        E6(new Vector2f(D6.position.translate(120.f,0.f))),
        E7(new Vector2f(D7.position.translate(120.f,0.f))),
        E8(new Vector2f(D8.position.translate(120.f,0.f))),

        F1(new Vector2f(E1.position.translate(120.f,0.f))),
        F2(new Vector2f(E2.position.translate(120.f,0.f))),
        F3(new Vector2f(E3.position.translate(120.f,0.f))),
        F4(new Vector2f(E4.position.translate(120.f,0.f))),
        F5(new Vector2f(E5.position.translate(120.f,0.f))),
        F6(new Vector2f(E6.position.translate(120.f,0.f))),
        F7(new Vector2f(E7.position.translate(120.f,0.f))),
        F8(new Vector2f(E8.position.translate(120.f,0.f))),

        G1(new Vector2f(F1.position.translate(120.f,0.f))),
        G2(new Vector2f(F2.position.translate(120.f,0.f))),
        G3(new Vector2f(F3.position.translate(120.f,0.f))),
        G4(new Vector2f(F4.position.translate(120.f,0.f))),
        G5(new Vector2f(F5.position.translate(120.f,0.f))),
        G6(new Vector2f(F6.position.translate(120.f,0.f))),
        G7(new Vector2f(F7.position.translate(120.f,0.f))),
        G8(new Vector2f(F8.position.translate(120.f,0.f))),

        H1(new Vector2f(G1.position.translate(120.f,0.f))),
        H2(new Vector2f(G2.position.translate(120.f,0.f))),
        H3(new Vector2f(G3.position.translate(120.f,0.f))),
        H4(new Vector2f(G4.position.translate(120.f,0.f))),
        H5(new Vector2f(G5.position.translate(120.f,0.f))),
        H6(new Vector2f(G6.position.translate(120.f,0.f))),
        H7(new Vector2f(G7.position.translate(120.f,0.f))),
        H8(new Vector2f(G8.position.translate(120.f,0.f))),
        ;

        private final Vector2f position;

        BoardLocations(Vector2f position) {
            this.position = position;
        }

        public Vector2f getPosition() {
            return position;
        }
    }
}
