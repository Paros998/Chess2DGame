package com.ourshipsgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.ourshipsgame.Main;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.hud.Hud;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * Klasa ekranu głównego gry
 */
public class GameScreen extends GameEngine implements InputProcessor {

    /**
     * Identyfikator klasy
     */
    private final String id = getClass().getName();
    /**
     * AssetManager do ładowania zasobów gry
     */
    public AssetManager manager;
    /**
     * Obiekt aplikacji
     */
    private Main game;
    /**
     * Obiekt ekranu głównego
     */
    private GameScreen GameScreen;
    /**
     * Multiplekser do obsługi wejścia (klawisze/myszka etc)
     */
    private InputMultiplexer inputMultiplexer;
    /**
     * Obiekt głowny interfejsu
     */
    private Hud hud;
    /**
     * Obiekt do rysowania na ekranie
     */
    private SpriteBatch sb;
    /**
     * Obiekt do rysowania kształtów na ekranie
     */
    private ShapeRenderer sr;
    /**
     * Zmienna przechowująca progres ładowania zasobów
     */
    private float progress;

    /**
     * Zmienna określająca ,który to stopień gry do obliczeń logiki gry
     */
    private int gameStage = 1;
    /**
     * Tło do ekranu ładowania
     */
    private Texture loadingTexture;
    /**
     * Zmienna określająca czy utworzono tekstury
     */
    private boolean createdTextures = false;
    /**
     * Zmienna określająca czy należy stworzyć okno dialogowe z użytkownikiem
     */
    private boolean createDialog = false;
    // other vars
    /**
     * Czcionka do ekranu ładowania
     */
    private BitmapFont font;

    // constructor

    /**
     * Konstruktor ekranu głównego
     *
     * @param game Obiekt aplikacji
     */
    public GameScreen(Main game) {
        this.GameScreen = this;
        this.game = game;
        Gdx.app.log(id, "This class is loaded!");
        System.out.println(Gdx.app.getJavaHeap() / 1000000);
    }

    // Draw methods

    /**
     * Metoda do renderowania mapy
     */
    private void drawMap() {
        gameBackground.getSprite().draw(sb);
        gameBoard.Board.getSprite().draw(sb);
    }

    /**
     * Metoda do renderowania statków i ich elementów
     */
    private void drawChessPieces() {
        for (int i = 0; i < 16; i++) {
            whiteChesses[i].getGameObject().getSprite().draw(sb);
//            Rectangle alignmentRectangle = whiteChesses[i].getGameObject().alignmentRectangle;
//            sr.rect(alignmentRectangle.x, alignmentRectangle.y, alignmentRectangle.width, alignmentRectangle.height,
//                    Color.GREEN,  Color.GREEN,  Color.GREEN,  Color.GREEN);

            blackChesses[i].getGameObject().getSprite().draw(sb);
//            alignmentRectangle = blackChesses[i].getGameObject().alignmentRectangle;
//            sr.rect(alignmentRectangle.x, alignmentRectangle.y, alignmentRectangle.width, alignmentRectangle.height,
//                    Color.GREEN,  Color.GREEN,  Color.GREEN,  Color.GREEN);
        }
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
                                    GameScreen.dispose();
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
    private void drawTurnInfo(SpriteBatch batch) {
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
     * Metoda do renderowania ekranu ładowania
     */
    private void drawLoadingScreen() {
        progress = manager.getProgress();
        sb.begin();
        sb.draw(loadingTexture, 0, 0);
        String load = "Loading " + NumberFormat.getPercentInstance().format(progress);
        font.draw(sb, load, (gameWidth_f / 2f) - 175, (gameHeight_f / 2f) + 43);
        sb.end();
    }

    /**
     * Metoda do renderowania wiadomości po bitwie
     */
    private void drawExitScreen() {
        String msg;
        if (PlayerOneLost) {
            msg = "You 've Lost!! Better luck next time!";
            font.draw(sb, msg, (gameWidth_f / 2) - 350, gameHeight_f / 2 + 400);
        } else if (PlayerTwoLost) {
            msg = "You 've Won!! Keep it up!!";
            font.draw(sb, msg, (gameWidth_f / 2) - 250, gameHeight_f / 2 + 400);
        }
    }

    // Create methods
    /**
     * Metoda do utworzenia czcionek
     */
    private void createFonts() {
        font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("core/assets/fonts/Raleway-ExtraLightItalic.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 43;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.WHITE;
        parameter.color = Color.RED;
        font = generator.generateFont(parameter);
        parameter.size = 16;
        parameter.borderWidth = 0;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.GOLD;
        hudFont = generator.generateFont(parameter);
        generator.dispose();
    }

    // method to create elements

    /**
     * Metoda do tworzenia wszystkich elementów graficznych gry
     */
    private void createGraphics() {
        // changing game stage from loading to playing
        if (preparation(true, manager)) {
            gameStage = 2;
            hud = new Hud(manager, game, GameScreen, cursor);
            createdTextures = true;
        }

        hud.gameSettings = game.menuElements.gameSettings;
        // Deleting GlobalMenuElements object
        game.menuElements = null;
        hud.getPlayButton().addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (gameStage == 3) {
                    hud.getStage().getActors().pop();
                    hud.getPlayersSetNameDialog().hide();
                    PlayerOne.setPlayerName(hud.getPlayersName());
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

    // loading method

    /**
     * Metoda do ładowania wszystkich zasobów gry
     */
    private void loadAssets() {
        loadGameEngine(manager);
        loadHudAssets(manager);
    }


    /**
     * Metoda do odtwarzania dźwięków ruchu pionków
     */
    private void playMoveSound() {

    }

    /**
     * Metoda do aktualizacji logiki gry
     *
     * @param deltaTime czas między klatkami
     */
    // update logics of game
    private void update(float deltaTime) {
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
                    if (enemyComputerPlayerAi != null) {

                    }
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

        if (manager.update()) {
            // When loading screen disappers
            if (!createdTextures) {
                loadingTexture.dispose();
                createGraphics();
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(this);
                inputMultiplexer.addProcessor(hud.getStage());
                Gdx.input.setInputProcessor(inputMultiplexer);
                // tmp
            }
            if (hud.isPasued())
                Gdx.input.setInputProcessor(hud.getStage());
            else
                Gdx.input.setInputProcessor(inputMultiplexer);


            // update
            update(deltaTime);
            // render things below
            sb.begin();
            sr.setAutoShapeType(true);
            sr.begin();
            // Do not place any drawings up!!

            drawMap();
            drawChessPieces();

            // Texts
            switch (gameStage) {
                case 2 -> drawStage2Text(font, sb);
                case 3 -> drawTurnInfo(sb);
                case 4 -> drawExitScreen();
            }

            sb.end();
            sr.end();
            hud.update();
        } else {
            // While loading the game assets
            drawLoadingScreen();
        }
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
        inputMultiplexer.clear();
        sb.dispose();
        sr.dispose();
        manager.dispose();
        loadingTexture.dispose();
        font.dispose();
        hud.gameSettings.dispose();
        hud.dispose();
        hudFont.dispose();
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
        switch (button) {
            case Buttons.LEFT:
                if (gameStage == 3) {
                    if (PlayerTurn == 1) {

                    }
                }
                break;
            case Buttons.RIGHT:
                if (gameStage == 3) {
                    if (PlayerTurn == 1) {

                    }
                }
                break;
        }
        return false;
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
