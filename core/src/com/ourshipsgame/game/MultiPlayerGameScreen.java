package com.ourshipsgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
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
import com.ourshipsgame.hud.Hud;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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


    // constructor

    /**
     * Konstruktor ekranu głównego
     *
     * @param game Obiekt aplikacji
     */
    public MultiPlayerGameScreen(Main game) {
        this.multiPlayerGameScreen = this;
        this.game = game;
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

    /**
     * Metoda do renderowania informacji o wynikach gracza i komputera
     *
     * @param batch SpriteBatch do renderowania
     */
    @Override
    protected void drawTurnInfo(SpriteBatch batch) {
        switch (PlayerTurn) {
            case 1 -> {
                turnFontActive.draw(batch, "Your Turn!", gameWidth_f / 2 - 250, gameHeight_f - 140);
                turnFont.draw(batch, "Enemy Turn!", gameWidth_f / 2 + 90, gameHeight_f - 140);
            }
            case 2 -> {
                turnFont.draw(batch, "Your Turn!", gameWidth_f / 2 - 250, gameHeight_f - 140);
                turnFontActive.draw(batch, "Enemy Turn!", gameWidth_f / 2 + 90, gameHeight_f - 140);
            }
        }
    }

    /**
     * Metoda do tworzenia wszystkich elementów graficznych gry
     */
    protected void createGraphics() {
        // changing game stage from loading to playing
        if (preparation(true, manager)) {
            gameStage = 2;
            hud = new Hud(manager, game, multiPlayerGameScreen, cursor);
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
                    PlayerOne.setPlayerName(hud.getPlayersName());
                    gameStage = 3;
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        hud.getRepeatButton().addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

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
        switch (gameStage){
            case 2 -> {

            }
            case 3 -> {
                if (PlayerTurn == 1)
                    PlayerOne.updateTime(deltaTime);
                else
                    PlayerTwo.updateTime(deltaTime);

                // Update AI info

                if (PlayerTurn == 2) {

                }
            }
            case 4 -> {
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
                if (gameStage == 3) {
                    if(!checkForMoveClicked(screenX, screenY))
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
            for (GameObject move: possibleMovesAndAttacks)
                if(move.contains(screenX,screenY)){
                    currentChessClicked.moveChess(getEnumByPosition(move.getPosition()), hud.gameSettings.soundVolume);
                    //later here will be some kind of changeTurn method called instead
                    currentChessClicked = null;
                    return true;
                }
            return false;
        }
        return false;
    }

    private void checkForChessClicked(int screenX, int screenY) {
        for (int i = 0; i < 16; i++){
            if(whiteChesses[i].clickedOnThisChess(screenX, screenY,gameBoard)){
                currentChessClicked = whiteChesses[i];
                return;
            }
            if(blackChesses[i].clickedOnThisChess(screenX, screenY,gameBoard)){
                currentChessClicked = blackChesses[i];
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
