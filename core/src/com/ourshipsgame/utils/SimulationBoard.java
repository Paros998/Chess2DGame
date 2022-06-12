package com.ourshipsgame.utils;

import com.ourshipsgame.chess_pieces.Chess;
import com.ourshipsgame.chess_pieces.King;
import com.ourshipsgame.chess_pieces.Pawn;
import com.ourshipsgame.game.Player;
import com.ourshipsgame.handlers.Constant;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationBoard {
    private final SimulationBoardLocation[][] board = new SimulationBoardLocation[8][8];

    private final SimulationChess[] whiteCheeses = new SimulationChess[16];
    private final SimulationChess[] blackCheeses = new SimulationChess[16];

    public SimulationBoardLocation getLocationByArrayPosition(Vector2i arrayPosition) {
        return board[arrayPosition.getX()][arrayPosition.getY()];
    }

    public SimulationBoard(Chess[] whiteCheeses, Chess[] blackCheeses) {

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                board[i][j] = new SimulationBoardLocation(new Vector2i(i, j));
            }

        AtomicInteger index = new AtomicInteger();

        Arrays.stream(whiteCheeses).forEach(chess -> {

                    Vector2i position = chess.getCurrentLocation().getArrayPosition();

                    if (chess instanceof Pawn pawn)

                        this.whiteCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.WHITE,
                                board[position.getX()][position.getY()],
                                chess.getClass(),
                                chess.isDestroyed(),
                                pawn.isFirstMove());

                    else if (chess instanceof King king)

                        this.whiteCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.WHITE,
                                board[position.getX()][position.getY()],
                                king.isWasCheckedTurnAgo(),
                                king.isChecked());

                    else
                        this.whiteCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.WHITE,
                                board[position.getX()][position.getY()],
                                chess.getClass(),
                                chess.isDestroyed());

                    board[position.getX()][position.getY()].setChess(this.whiteCheeses[index.get()], true);

                    index.getAndIncrement();

                }
        );

        index.set(0);

        Arrays.stream(blackCheeses).forEach(chess -> {

                    Vector2i position = chess.getCurrentLocation().getArrayPosition();

                    if (chess instanceof Pawn pawn)

                        this.blackCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.BLACK,
                                board[position.getX()][position.getY()],
                                chess.getClass(),
                                chess.isDestroyed(),
                                pawn.isFirstMove());

                    else if (chess instanceof King king)

                        this.blackCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.WHITE,
                                board[position.getX()][position.getY()],
                                king.isWasCheckedTurnAgo(),
                                king.isChecked());

                    else
                        this.blackCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.BLACK,
                                board[position.getX()][position.getY()],
                                chess.getClass(),
                                chess.isDestroyed());

                    board[position.getX()][position.getY()].setChess(this.blackCheeses[index.get()], true);

                    index.getAndIncrement();

                }
        );

        evaluateAllMoves();

    }

    public SimulationBoard(SimulationChess[] whiteCheeses, SimulationChess[] blackCheeses) {

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                board[i][j] = new SimulationBoardLocation(new Vector2i(i, j));
            }

        AtomicInteger index = new AtomicInteger();

        Arrays.stream(whiteCheeses).forEach(chess -> {

                    Vector2i position = new Vector2i();
                    if (!chess.isDestroyed())
                        position = chess.getCurrentLocation().getArrayPosition();

                    if (chess.getClazz().equals(Pawn.class))

                        this.whiteCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.WHITE,
                                chess.isDestroyed() ? null : board[position.getX()][position.getY()],
                                chess.getClazz(),
                                chess.isDestroyed(),
                                chess.isFirstMove());

                    else if (chess.getClazz().equals(King.class))

                        this.whiteCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.WHITE,
                                board[position.getX()][position.getY()],
                                chess.isWasCheckedTurnAgo(),
                                chess.isChecked());

                    else
                        this.whiteCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.WHITE,
                                chess.isDestroyed() ? null : board[position.getX()][position.getY()],
                                chess.getClazz(),
                                chess.isDestroyed());

                    if (!chess.isDestroyed())
                        board[position.getX()][position.getY()].setChess(this.whiteCheeses[index.get()], true);

                    index.getAndIncrement();

                }
        );

        index.set(0);

        Arrays.stream(blackCheeses).forEach(chess -> {

                    Vector2i position = new Vector2i();
                    if (!chess.isDestroyed())
                        position = chess.getCurrentLocation().getArrayPosition();

                    if (chess.getClazz().equals(Pawn.class))

                        this.blackCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.BLACK,
                                chess.isDestroyed() ? null : board[position.getX()][position.getY()],
                                chess.getClazz(),
                                chess.isDestroyed(),
                                chess.isFirstMove());

                    else if (chess.getClazz().equals(King.class))

                        this.blackCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.BLACK,
                                board[position.getX()][position.getY()],
                                chess.isWasCheckedTurnAgo(),
                                chess.isChecked());

                    else
                        this.blackCheeses[index.get()] = new SimulationChess(
                                Player.PlayerColor.BLACK,
                                chess.isDestroyed() ? null : board[position.getX()][position.getY()],
                                chess.getClazz(),
                                chess.isDestroyed());

                    if (!chess.isDestroyed())
                        board[position.getX()][position.getY()].setChess(this.blackCheeses[index.get()], true);

                    index.getAndIncrement();

                }
        );

        evaluateAllMoves();

    }

    public void evaluateAllMoves() {

        for (int i = 1; i < 16; i++) {
            if (!this.whiteCheeses[i].isDestroyed())
                this.whiteCheeses[i].evaluateMoves(this);
            if (!this.blackCheeses[i].isDestroyed())
                this.blackCheeses[i].evaluateMoves(this);
        }

        this.whiteCheeses[0].evaluateMoves(this);
        this.blackCheeses[0].evaluateMoves(this);

        this.whiteCheeses[Constant.ChessPiecesInArray.King.ordinal()].checkKingCondition(blackCheeses, this);
        this.blackCheeses[Constant.ChessPiecesInArray.King.ordinal()].checkKingCondition(whiteCheeses, this);
    }

    public SimulationChess getWhiteKing() {
        return whiteCheeses[Constant.ChessPiecesInArray.King.ordinal()];
    }

    public SimulationChess getBlackKing() {
        return blackCheeses[Constant.ChessPiecesInArray.King.ordinal()];
    }

    public SimulationBoardLocation[][] getBoard() {
        return board;
    }

    public SimulationChess[] getWhiteCheeses() {
        return whiteCheeses;
    }

    public SimulationChess[] getBlackCheeses() {
        return blackCheeses;
    }


}
