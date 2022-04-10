package com.ourshipsgame.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ourshipsgame.Main;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.hud.GameTextButton;

public class SelectGameModeScreen implements Screen, Constant {
    /**
     * Obiekt klasy Main. Odpowiedzialny głównie za zarządzanie ekranami. W tej
     * klasie jest również odwołaniem do klasy MenuGlobalElements.
     */
    private final Main game;

    /**
     * Scena z silnika libGDX. Sprawia, że elementy w grze są interaktywne oraz
     * rysuje je na ekranie.
     */
    public Stage stage;

    /**
     * Obiekt silnika libGDX. Rysuje obiekty na ekranie tj. sprite'y czy tekstury.
     */
    public SpriteBatch batch;

    // Constructor

    /**
     * Główny i jedyny konstruktor klasy OptionScreen.
     *
     * @param game Obiekt klasy Main.
     */
    public SelectGameModeScreen(Main game) {
        this.game = game;
    }

    // Methods

    /**
     * Metoda odopwiedzialna za wybór trybu grania
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        float scale = 1.3f;

        /*
         * Przycisk z libGDX. Włącza tryb grania z komputerem.
         */
        GameTextButton playSingleButton = new GameTextButton(
                "NEW GAME",
                GAME_WIDTH_F / 2,
                GAME_HEIGHT_F / 2,
                game.menuElements.skin,
                7,
                game,
                scale
        );

        /*
         * Przycisk z libGDX. Włącza tryb grania z drugim graczem.
         */
        GameTextButton loadExistingGameButton = new GameTextButton(
                "LOAD GAME",
                GAME_WIDTH_F / 2,
                GAME_HEIGHT_F / 2 - 100,
                game.menuElements.skin,
                8,
                game,
                scale
        );

        /*
         * Przycisk z libGDX. Włącza tryb grania z drugim graczem.
         */
        GameTextButton playMultiButton = new GameTextButton(
                "MULTIPLAYER",
                GAME_WIDTH_F / 2,
                GAME_HEIGHT_F / 2 - 200,
                game.menuElements.skin,
                9,
                game,
                scale
        );

        /*
         * Przycisk z libGDX. Wraca do głównego okna menu gry.
         */
        GameTextButton backButton = new GameTextButton(
                "BACK",
                GAME_WIDTH_F / 2,
                GAME_HEIGHT_F / 2 - 300,
                game.menuElements.skin,
                6,
                game,
                scale
        );

        stage.addActor(playSingleButton);
        stage.addActor(loadExistingGameButton);
        stage.addActor(playMultiButton);
        stage.addActor(backButton);
    }

    /**
     * Metoda odpowiedzialna za odświeżanie opreacji w oknie opcji.
     *
     * @param deltaTime Główny czas silnika libGDX.
     */
    private void update(float deltaTime) {
        game.menuElements.moveMenu(deltaTime);
        stage.act();
    }

    /**
     * Metoda odpowiedzialna za renderowanie okna opcji (libGDX).
     *
     * @param delta Główny czas silnika libGDX.
     */
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(game.menuElements.menuTexture.getTexture(), game.menuElements.menuTexture.x,
                game.menuElements.menuTexture.y);

        batch.end();
        stage.draw();
    }

    /**
     * Metoda obsługująca skalowanie okna gry (libGDX).
     *
     * @param width  Szerokość okna.
     * @param height Wysokość okna.
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Metoda obsługująca pauzę w grze (libGDX).
     */
    @Override
    public void pause() {

    }

    /**
     * Metoda obsługująca wyłączenie pauzy (libGDX).
     */
    @Override
    public void resume() {

    }

    /**
     * Metoda obsługująca ukrycie okna gry (libGDX).
     */
    @Override
    public void hide() {
        dispose();
    }

    /**
     * Metoda obsługująca niszczenie elementów silnika libGDX.
     */
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
