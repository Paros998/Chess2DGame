package com.ourshipsgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.ourshipsgame.Main;
import com.ourshipsgame.chess_pieces.Chess;
import com.ourshipsgame.chess_pieces.Pawn;
import com.ourshipsgame.hud.Hud;
import com.ourshipsgame.inteligentSystems.ComputerPlayerAi;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;
import com.ourshipsgame.utils.ChessMove;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static com.ourshipsgame.game.GameBoard.BoardLocations.getEnumByPosition;

/**
 * Klasa ekranu głównego gry
 */
public class SinglePlayerGameScreen extends GameEngine implements InputProcessor {

    /**
     * Identyfikator klasy
     */
    private final String id = getClass().getName();

    /**
     * Obiekt ekranu głównego
     */
    private final SinglePlayerGameScreen SinglePlayerGameScreen;

    /**
     * Konstruktor ekranu głównego
     *
     * @param game                   Obiekt aplikacji
     * @param shouldLoadGameFromFile should load
     */
    public SinglePlayerGameScreen(Main game, boolean shouldLoadGameFromFile) {
        this.SinglePlayerGameScreen = this;
        this.game = game;
        Gdx.app.log(id, "This class is loaded!");
        System.out.println(Gdx.app.getJavaHeap() / 1000000);
        this.loadGameFromFile = shouldLoadGameFromFile;
    }

    // Draw methods
    private void addHistory(GameBoard.BoardLocations moveFrom, GameBoard.BoardLocations moveTo, ChessMove.typesOfMoves type, ChessMove.pieceType piece) {
        gameHistory.updateHistoryAfterTurn(new ChessMove(moveFrom, moveTo, type, piece));
    }

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
                                    SinglePlayerGameScreen.dispose();
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

    private void randomizeStart() {
        Random random = new Random(System.currentTimeMillis());

        if (random.nextInt(2) == 1) {
            MyPlayer = whitePlayer;
            EnemyPlayer = blackPlayer;
        } else {
            MyPlayer = blackPlayer;
            EnemyPlayer = whitePlayer;
        }

        MyPlayer.setPlayerName("TemplateName");
        EnemyPlayer.setPlayerName("Bot Clark");

        if (EnemyPlayer == blackPlayer)
            enemyComputerPlayerAi = new ComputerPlayerAi(whiteCheeses, blackCheeses);
        else
            enemyComputerPlayerAi = new ComputerPlayerAi(blackCheeses, whiteCheeses);

        enemyComputerPlayerAi.setMyPlayer(EnemyPlayer);
    }

    /**
     * Metoda do tworzenia wszystkich elementów graficznych gry
     */
    @Override
    protected void createGraphics() {
        // changing game stage from loading to playing
        if (preparation(manager)) {

            if (!loadGameFromFile || !new File("gameSave.txt").exists()) {
                randomizeStart();
                gameHistory = new GameHistory(MyPlayer, whitePlayer, blackPlayer);
                loadGameFromFile = false;
                gameStage = 2;
            } else {
                gameHistory = new GameHistory(whitePlayer, blackPlayer);
                loadGameFromFile();
                gameStage = 3;
            }

            calculateChessMoves();

            hud = new Hud(manager, game, SinglePlayerGameScreen, cursor, loadGameFromFile);
            createdTextures = true;
        }

        hud.gameSettings = game.menuElements.gameSettings;
        // Deleting GlobalMenuElements object
        game.menuElements = null;

        hud.getPlayButton().addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (gameStage == 2) {
                    hud.getStage().getActors().pop();
                    hud.getPlayersSetNameDialog().hide();

                    if (!loadGameFromFile)
                        MyPlayer.setPlayerName(hud.getPlayersName());

                    gameStage = 3;
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
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

            case 3 -> {
                if (!pause) {

                    changeLastMoveOpacity(deltaTime);

                    if (PlayerTurn.equals(MyPlayer))
                        MyPlayer.updateTime(deltaTime);
                    else {
                        EnemyPlayer.updateTime(deltaTime);

                        if (enemyComputerPlayerAi != null) {

                            enemyComputerPlayerAi.update(deltaTime);

                            if (!enemyComputerPlayerAi.getIsCalculating().get() && !enemyComputerPlayerAi.getReadyToMove().get())
                                enemyComputerPlayerAi.calculateMove();

                            if (enemyComputerPlayerAi.getReadyToMove().get()) {

                                enemyComputerPlayerAi.getMovableChess()
                                        .moveChess(
                                                enemyComputerPlayerAi.getMoveDestination(),
                                                hud.gameSettings.soundVolume
                                        );

                                if (enemyComputerPlayerAi.getMovableChess() instanceof Pawn pawn) {
                                    if (pawn.checkIfReachedEnd()) {
                                        pawnToChange = pawn;
                                        String figure = enemyComputerPlayerAi.choosePawn();
                                        changePawn(figure);

                                        addHistory(
                                                enemyComputerPlayerAi.getMoveStart(),
                                                enemyComputerPlayerAi.getMoveDestination(),
                                                ChessMove.typesOfMoves.CHANGEFIGURE,
                                                ChessMove.pieceType.valueOf(figure.trim())
                                        );

                                    } else addHistory(
                                            enemyComputerPlayerAi.getMoveStart(),
                                            enemyComputerPlayerAi.getMoveDestination(),
                                            ChessMove.typesOfMoves.NORMAL,
                                            ChessMove.pieceType.B_NOCHANGE
                                    );

                                } else addHistory(
                                        enemyComputerPlayerAi.getMoveStart(),
                                        enemyComputerPlayerAi.getMoveDestination(),
                                        ChessMove.typesOfMoves.NORMAL,
                                        ChessMove.pieceType.B_NOCHANGE
                                );

                                switchTurn();
                                enemyComputerPlayerAi.getReadyToMove().set(false);
                            }
                        }
                    }
                }
            }

            case 4 -> {
                if (!createDialog) {
                    boolean imPlayerOne = MyPlayer == whitePlayer;

                    if (PlayerOneLost) {
                        if (imPlayerOne)
                            endSounds[1].play(hud.gameSettings.soundVolume);
                        else endSounds[0].play(hud.gameSettings.soundVolume);
                    } else if (PlayerTwoLost) {
                        if (imPlayerOne)
                            endSounds[0].play(hud.gameSettings.soundVolume);
                        else endSounds[1].play(hud.gameSettings.soundVolume);
                    }

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
                if (gameStage == 3) {
                    if (PlayerTurn.equals(MyPlayer))
                        if (!checkForMoveClicked(screenX, screenY))
                            checkForChessClicked(screenX, screenY);
                }
                break;
            case Buttons.RIGHT:
                if (gameStage == 3) {
                    currentChessClicked = null;
                }
                break;
        }
        return false;
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

                        } else addHistory(
                                currentLocation,
                                getEnumByPosition(move.getPosition()),
                                ChessMove.typesOfMoves.NORMAL,
                                ChessMove.pieceType.B_NOCHANGE
                        );

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
        Chess[] cheesesToCheck;

        if (MyPlayer.equals(whitePlayer))
            cheesesToCheck = whiteCheeses;
        else cheesesToCheck = blackCheeses;

        for (int i = 0; i < 16; i++)
            if (cheesesToCheck[i].clickedOnThisChess(screenX, screenY, gameBoard)) {
                currentChessClicked = cheesesToCheck[i];
                return;
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
