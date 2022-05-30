package com.ourshipsgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.ourshipsgame.Main;
import com.ourshipsgame.chess_pieces.King;
import com.ourshipsgame.chess_pieces.Pawn;
import com.ourshipsgame.hud.Hud;
import com.ourshipsgame.inteligentSystems.ComputerPlayerAi;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;
import com.ourshipsgame.utils.ChessMove;
import org.lwjgl.util.vector.Vector2f;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

import static com.ourshipsgame.game.GameBoard.BoardLocations.getEnumByPosition;

/**
 * Klasa ekranu głównego gry
 */
public class MultiPlayerGameScreen extends GameEngine implements InputProcessor {

    /**
     * Identyfikator klasy
     */
    private final String id = getClass().getName();

    /**
     * Obiekt ekranu głównego
     */
    private final MultiPlayerGameScreen multiPlayerGameScreen;

    private final boolean isHost;

    private boolean isClientConnected = false;
    private boolean isClientFirstTurn = true;

    private Socket serverSender;
    private Socket clientSender;


    // constructor

    /**
     * Konstruktor ekranu głównego
     *
     * @param game Obiekt aplikacji
     */
    public MultiPlayerGameScreen(Main game, boolean isHost) {
        this.multiPlayerGameScreen = this;
        this.game = game;
        this.isHost = isHost;
        Gdx.app.log(id, "This class is loaded!");
        System.out.println(Gdx.app.getJavaHeap() / 1000000);
    }

    // Draw methods


    /**
     * Metoda do stworzenia okna dialogowego po skończonej bitwie
     */
    private void createDialog() {
        new Dialog("Do you wish to save score?", hud.getSkin()) {
            {
                this.button("Yes", "Yes");
                this.button("No", "No");
            }

            @Override
            protected void result(Object object) {
                switch (object.toString()) {
                    case "Yes" -> {
                        File file = new File("scores.txt");
                        if (!file.exists())
                            try {
                                file.createNewFile();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        try {
                            FileWriter writer = new FileWriter(file, true);
                            writer.write(PlayerOne.getPlayerName() + " ");
                            writer.write(PlayerOne.getScoreValue() + " ");
                            writer.write(PlayerOne.getTimeElapsed() + " ");
                            writer.write(PlayerOne.getAccuracyRatio() + " ");
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    case "No" -> new Dialog("What now?", hud.getSkin()) {
                        {

                            this.button("Main menu!", "menu");
                            this.button("Exit game!", "game");
                        }

                        @Override
                        protected void result(Object object) {
                            switch (object.toString()) {
                                case "menu" -> {
                                    multiPlayerGameScreen.dispose();
                                    game.menuElements = new MenuGlobalElements(game);
                                    game.setScreen(new MenuScreen(game));
                                }
                                case "game" -> Gdx.app.exit();
                            }
                        }
                    }.show(hud.getStage());
                }
            }
        }.show(hud.getStage());
    }

    private void initStart() {
        if (isHost) {
            MyPlayer = whitePlayer;
            EnemyPlayer = blackPlayer;
            MyPlayer.setPlayerName("Host");
            EnemyPlayer.setPlayerName("Client");
            isClientFirstTurn = false;
        } else {
            MyPlayer = blackPlayer;
            EnemyPlayer = whitePlayer;
            MyPlayer.setPlayerName("Client");
            EnemyPlayer.setPlayerName("Host");
        }
    }

    private void initServer() {
        new Thread(() -> {
            if (isHost) {
                ServerSocketHints hints = new ServerSocketHints();
                hints.acceptTimeout = 100000000;
                ServerSocket server = Gdx.net.newServerSocket(Net.Protocol.TCP, "localhost", 8080, hints);
                serverSender = server.accept(null);
                try {
                    String message = new BufferedReader(new InputStreamReader(serverSender.getInputStream())).readLine();
                    Gdx.app.log("PingPongSocketExample", "got client message: " + message);
                    serverSender.getOutputStream().write("PONG\n".getBytes());
                    isClientConnected = true;
                } catch (IOException e) {
                    Gdx.app.log("PingPongSocketExample", "an error occured", e);
                    isClientConnected = false;
                }
            } else {
                SocketHints hints = new SocketHints();
                clientSender = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 8080, hints);
                try {
                    clientSender.getOutputStream().write("PING\n".getBytes());
                    String response = new BufferedReader(new InputStreamReader(clientSender.getInputStream())).readLine();
                    Gdx.app.log("PingPongSocketExample", "got server message: " + response);
                } catch (IOException e) {
                    Gdx.app.log("PingPongSocketExample", "an error occured", e);
                }
            }
        }).start();
    }

    private void drawWaitingForClient(BitmapFont font, SpriteBatch batch) {
        int fontSize = 20;
        if (isHost) {
            waitingForClientMessageBackground.drawSprite(batch);
            Vector2f waitingForClientMessageBackgroundPosition = waitingForClientMessageBackground.getPosition();

            String text = isClientConnected ? "Connected! Click a button to start the game" : "Waiting for player to join...";
            int len = text.length();
            font.draw(batch,
                    text,
                    (waitingForClientMessageBackgroundPosition.getX() + (waitingForClientMessageBackground.getWidth() / 2) - (fontSize * (len / 2.f))),
                    waitingForClientMessageBackgroundPosition.getY() + 165 + 25);
        } else {
            stage2MessageBackground.drawSprite(batch);
            Vector2f stage2MessageBackgroundPosition = stage2MessageBackground.getPosition();

            String text = "Waiting for server to start the game...";
            int len = text.length();
            font.draw(batch,
                    text,
                    (stage2MessageBackgroundPosition.getX() + (stage2MessageBackground.getWidth() / 2) - (fontSize * (len / 2.f))),
                    stage2MessageBackgroundPosition.getY() + 165 + 25);
        }
    }

    @Override
    protected void createAndDisplay(float deltaTime, InputProcessor processor) {
        if (manager.update()) {
            // When loading screen disappers
            if (!createdTextures) {
                loadingTexture.dispose();
                createGraphics();
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(processor);
                inputMultiplexer.addProcessor(hud.getStage());
                Gdx.input.setInputProcessor(inputMultiplexer);
            }
            if (hud.isPaused())
                Gdx.input.setInputProcessor(hud.getStage());
            else
                Gdx.input.setInputProcessor(inputMultiplexer);

            // render things below
            sb.begin();
            sr.setAutoShapeType(true);
            sr.begin();
            // Do not place any drawings up!!

            // Texts
            switch (gameStage) {
                case 2 -> {
                    drawMap();
                    drawChessPieces();
                    drawWaitingForClient(font, sb);
                }
                case 4 -> {
                    drawMap();
                    drawCurrentClickedChessAvailableMoves();
                    drawChessPieces();
                    drawTurnInfo(sb);
                    drawKingCondition(sb);
                }
                case 5 -> {
                    drawMap();
                    drawChessPieces();
                    drawExitScreen();
                }
            }

            sb.end();
            sr.end();
            hud.update();
            update(deltaTime);
        } else {
            // While loading the game assets
            drawLoadingScreen();
        }
    }

    /**
     * Metoda do tworzenia wszystkich elementów graficznych gry
     */
    protected void createGraphics() {
        // changing game stage from loading to playing
        if (preparation(manager)) {
            initStart();
            initServer();
            gameStage = 2;
            calculateChessMoves();
            hud = new Hud(manager, game, multiPlayerGameScreen, cursor, isHost);
            createdTextures = true;
            gameHistory = new GameHistory(whitePlayer, blackPlayer);
        }

        hud.gameSettings = game.menuElements.gameSettings;
        // Deleting GlobalMenuElements object
        game.menuElements = null;

        hud.getPlayButton().addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (gameStage == 2 && isHost && isClientConnected) {
                    gameStage = 3;
                }

                if (gameStage == 3 && isHost) {
                    try {
                        serverSender.getOutputStream().write("start\n".getBytes());
                        hud.getStage().getActors().removeIndex(0);
                        gameStage = 4;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    @Override
    protected void switchTurn() {
        if (PlayerTurn == MyPlayer)
            PlayerTurn = EnemyPlayer;
        else
            PlayerTurn = MyPlayer;

        calculateChessMoves();

        King whiteKing = (King) whiteCheeses[ChessPiecesInArray.King.ordinal()];
        whiteKing.checkKingCondition(blackCheeses, gameBoard);

        King blackKing = (King) blackCheeses[ChessPiecesInArray.King.ordinal()];
        blackKing.checkKingCondition(whiteCheeses, gameBoard);

        PlayerOneChecked = whiteKing.isChecked();
        PlayerTwoChecked = blackKing.isChecked();

        PlayerOneLost = whiteKing.isMated();
        PlayerTwoLost = blackKing.isMated();

        TieBetweenPlayers = whiteKing.isTie() | blackKing.isTie() | (PlayerOneLost && PlayerTwoLost);

        if (PlayerOneLost | PlayerTwoLost | TieBetweenPlayers)
            gameStage = 5;

    }

    private void sendMove() {
        ChessMove lastMove = gameHistory.getLastMove();

        if (isHost) {
            Thread receiver = new Thread(() -> {
                try {
                    serverSender.getOutputStream().write((lastMove.write() + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (!receiver.isAlive()) {
                receiver.start();
            }
        } else {
            Thread receiver = new Thread(() -> {
                try {
                    clientSender.getOutputStream().write((lastMove.write() + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (!receiver.isAlive()) {
                receiver.start();
            }

            isClientFirstTurn = false;
        }
    }


    private void receiveMove() {
        if (isHost) {
            Thread receiver = new Thread(() -> {
                try {
                    String response = new BufferedReader(new InputStreamReader(serverSender.getInputStream())).readLine();

                    ChessMove lastMove = ChessMove.readFromLine(response);

                    loadMove(lastMove);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (!receiver.isAlive()) {
                receiver.start();
            }
        } else {
            Thread receiver = new Thread(() -> {
                try {
                    String response = new BufferedReader(new InputStreamReader(clientSender.getInputStream())).readLine();

                    ChessMove lastMove = ChessMove.readFromLine(response);

                    loadMove(lastMove);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (!receiver.isAlive()) {
                receiver.start();
            }
        }
    }


    /**
     * Metoda do aktualizacji logiki gry
     *
     * @param deltaTime czas między klatkami
     */
    // update logics of game
    @Override
    protected void update(float deltaTime) {
        switch (gameStage) {
            case 2 -> {
                if (!isHost) {
                    Thread receiver = new Thread(() -> {
                        try {
                            String response = new BufferedReader(new InputStreamReader(clientSender.getInputStream())).readLine();
                            if (response.equals("start")) {
                                hud.getStage().getActors().removeIndex(0);
                                gameStage = 4;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    if (!receiver.isAlive()) {
                        receiver.start();
                    }
                }
            }
            case 4 -> {
                if (PlayerTurn == MyPlayer)
                    MyPlayer.updateTime(deltaTime);
                else
                    EnemyPlayer.updateTime(deltaTime);

                if (PlayerTurn == EnemyPlayer) {
                    receiveMove();
                    if(!isClientFirstTurn)
                        switchTurn();
                }

            }
            case 5 -> {
                if (!createDialog) {
                    if (PlayerOneLost)
                        endSounds[1].play(hud.gameSettings.soundVolume);
                    else if (PlayerTwoLost)
                        endSounds[0].play(hud.gameSettings.soundVolume);
                    createDialog();
                    createDialog = true;
                    Gdx.graphics.setCursor(cursor);
                }
            }
        }

    }

    /**
     * Metoda do renderowania całej szaty graficznej gry
     *
     * @param deltaTime czas między klatkami
     */
    // game loop method
    @Override
    public void render(float deltaTime) {
        // buffer screen
        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        createAndDisplay(deltaTime, this);
    }

    /**
     * Metoda wywoływana po pokazaniu ekranu gry
     */
    @Override
    public void show() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        manager = new AssetManager();
        loadingTexture = new Texture("core/assets/backgroundtextures/ChessMenuBg.png");
        createFonts();
        loadAssets();
    }

    /**
     * Metoda wywoływana gdy ekran jest zapauzowany
     */
    @Override
    public void pause() {
        super.pause();
    }

    /**
     * Metoda wywoływania gdy ekran wraca do aktywności
     */
    @Override
    public void resume() {
        super.resume();
    }

    /**
     * Metoda wywoływana gdy rozmiar okna jest zmieniany
     *
     * @param width  Nowa szerokość okna
     * @param height Nowa wysokość okna
     */
    @Override
    public void resize(int width, int height) {
        gameHeight = height;
        gameWidth = width;
        gameHeight_f = (float) gameHeight;
        gameWidth_f = (float) gameWidth;
        super.resize(width, height);
    }

    /**
     * Metoda wywoływana gdy ekran zwalnia wszystkie zasoby
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Metoda wywoływana gdy ekran jest chowany
     */
    @Override
    public void hide() {
        super.hide();
    }

    /**
     * Metoda wywoływana gdy klawisz jest wciskany
     *
     * @param keycode Oznaczenie klawisza
     * @return boolean
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Metoda wywoływana gdy klawisz jest puszczany
     *
     * @param keycode Oznaczenie klawisza
     * @return boolean
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Metoda wywoływana gdy znak klawiatury zostanie wciśnięty
     *
     * @param character Znak
     * @return boolean
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Metoda wywoływana gdy klawisz myszki zostanie wciśnięty
     *
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @param pointer Wskaźnik na coś..
     * @param button  Oznaczenie przycisku który został wciśnięty
     * @return boolean
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = 1080 - screenY;
        switch (button) {
            case Buttons.LEFT:
                if (gameStage == 4) {
                    if (!checkForMoveClicked(screenX, screenY))
                        checkForChessClicked(screenX, screenY);
                    else sendMove();
                }
                break;
            case Buttons.RIGHT:
                if (gameStage == 4) {
                    currentChessClicked = null;
                }
                break;
        }
        return false;
    }

    private void addHistory(GameBoard.BoardLocations moveFrom, GameBoard.BoardLocations moveTo, ChessMove.typesOfMoves type, ChessMove.pieceType piece) {
        gameHistory.updateHistoryAfterTurn(new ChessMove(moveFrom, moveTo, type, piece));
    }

    private boolean checkForMoveClicked(int screenX, int screenY) {
        if (currentChessClicked != null) {
            GameObject[] possibleMovesAndAttacks = currentChessClicked.getPossibleMovesAndAttacks();
            for (GameObject move : possibleMovesAndAttacks)
                if (move.contains(screenX, screenY)) {

                    GameBoard.BoardLocations currentLocation = currentChessClicked.getCurrentLocation();

                    if (!currentChessClicked.moveChess(getEnumByPosition(move.getPosition()), hud.gameSettings.soundVolume))
                        return false;

                    if (currentChessClicked instanceof Pawn pawn) {
                        if (pawn.checkIfReachedEnd()) {
                            pawnMoveStart = currentLocation;
                            pawnToChange = pawn;
                            hud.pawnChangeDialog.show(hud.getStage());
                            pause();

                        }

                    } else addHistory(
                            currentLocation,
                            getEnumByPosition(move.getPosition()),
                            ChessMove.typesOfMoves.NORMAL,
                            ChessMove.pieceType.B_NOCHANGE
                    );

                    currentChessClicked = null;
                    switchTurn();
                    return true;
                }
            return false;
        }
        return false;
    }

    private void checkForChessClicked(int screenX, int screenY) {
        for (int i = 0; i < 16; i++)
            if (isHost) {
                if (whiteCheeses[i].clickedOnThisChess(screenX, screenY, gameBoard)) {
                    currentChessClicked = whiteCheeses[i];
                    return;
                }
            } else {
                if (blackCheeses[i].clickedOnThisChess(screenX, screenY, gameBoard)) {
                    currentChessClicked = blackCheeses[i];
                    return;
                }
            }

    }

    /**
     * Metoda wywoływana gdy klawisz myszki zostanie upuszczony
     *
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @param pointer Wskaźnik na coś..
     * @param button  Oznaczenie przycisku który został wciśnięty
     * @return boolean
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    /**
     * Metoda wywoływana gdy klawisz myszki został wciśnięty i ruszony po ekranie
     *
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @param pointer Wskaźnik na coś..
     * @return boolean
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Metoda wywoływana gdy mysz zostanie ruszona na ekranie
     *
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @return boolean
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Metoda wywoływana gdy scroll myszki będzie aktywowany
     *
     * @param amountX ilość obrotu w poziomie
     * @param amountY ilość obrotu w pionie
     * @return boolean
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
