package com.ourshipsgame.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ourshipsgame.Main;
import com.ourshipsgame.game.GameObject;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.hud.GameTextButton;
import org.lwjgl.util.vector.Vector2f;

/**
 * Klasa zawierająca główne menu gry.
 */
public class MenuScreen implements Screen, Constant {

    /**
     * Obiekt klasy Main. Odpowiedzialny głównie za zarządzanie ekranami. W tej
     * klasie jest również odwołaniem do klasy MenuGlobalElements.
     */
    private Main game;

    /**
     * Scena z silnika libGDX. Sprawia, że elementy w grze są interaktywne oraz
     * rysuje je na ekranie.
     */
    public Stage stage;

    /**
     * Obiekt silnika libGDX. Rysuje obiekty na ekranie tj. sprite'y czy tekstury.
     */
    public SpriteBatch batch;

    /**
     * Obiekt klasy GameObject. Jest to logo w głównym menu gry.
     */
    private GameObject ChessLogo;

    /**
     * Przycisk w głównym menu gry. Jest przejściem do ekranu rozgrywki. Przyciski w
     * głównym menu gry. Są przejściem do ekranu kolejno rozgrywki, pomocy, wyników,
     * opcji. Przycisk ostatni to wyjście z gry.
     */
    private GameTextButton playButton;

    /**
     * Przycisk w głównym menu gry. Jest przejściem do ekranu pomocy.
     */
    private GameTextButton helpButon;

    /**
     * Przycisk w głównym menu gry. Jest przejściem do ekranu wyników.
     */
    private GameTextButton scoreButton;

    /**
     * Przycisk w głównym menu gry. Jest przejściem do ekranu opcji.
     */
    private GameTextButton optionsButton;

    /**
     * Przycisk w głównym menu gry. Wyłącza grę.
     */
    private GameTextButton quitButton;

    /**
     * Główny i jedyny konstruktor klasy MenuScreen.
     *
     * @param game Obiekt klasy Main.
     */
    public MenuScreen(Main game) {
        this.game = game;
    }

    /**
     * Metoda odpowiedzialna za odświeżanie opreacji w menu gry.
     *
     * @param deltaTime Główny czas silnika libGDX.
     */
    private void update(float deltaTime) {
        stage.act();
        game.menuElements.moveMenu(deltaTime);
    }

    private int currentMenuStage = 1;

    /**
     * Metoda odpowiedzialna za renderowanie menu gry (libGDX).
     *
     * @param deltaTime Główny czas silnika libGDX.
     */
    @Override
    public void render(float deltaTime) {
        // Updating menu
        update(deltaTime);

        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rendering menu
        batch.begin();

        batch.draw(game.menuElements.menuTexture.getTexture(), game.menuElements.menuTexture.x,
                game.menuElements.menuTexture.y);

        ChessLogo.drawSprite(batch);

        game.menuElements.font.draw(batch, game.menuElements.layout,
                GAME_WIDTH_F / 2 - game.menuElements.layout.width / 2, GAME_HEIGHT - 50);

        batch.end();
        stage.draw();
    }

    /**
     * Metoda odopwiedzialna za tworzenie, ustawianie i ładowanie elementów w
     * głównym menu gry (libGDX).
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        // Creating ChessLogo
        ChessLogo = new GameObject(new Texture("core/assets/backgroundtextures/chessLogo.png"), 0, 0, true, false,
                null);
        ChessLogo.getSprite().setSize(ChessLogo.width / 2, ChessLogo.height / 2);
        ChessLogo.getSprite().setScale(2.f, 2.f);
        ChessLogo.getSprite().setX(GAME_WIDTH_F / 2 - ChessLogo.getSprite().getWidth() / 2 + 50);
        ChessLogo.getSprite().setY(GAME_HEIGHT_F / 2 - ChessLogo.getSprite().getHeight() / 2 + 370);

        float scale = 1.3f;


        // Creating buttons
        playButton = new GameTextButton("PLAY", GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2, game.menuElements.skin, 1, game, scale);

        helpButon = new GameTextButton("HELP", GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2 - 100, game.menuElements.skin, 2, game, scale);

        scoreButton = new GameTextButton("SCORE", GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2 - 200, game.menuElements.skin, 3,
                game, scale);

        optionsButton = new GameTextButton("OPTIONS", GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2 - 300, game.menuElements.skin, 4,
                game, scale);

        quitButton = new GameTextButton("EXIT", GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2 - 400, game.menuElements.skin, 5, game, scale);

        // Adding actors to scene
        stage.addActor(playButton);
        stage.addActor(helpButon);
        stage.addActor(scoreButton);
        stage.addActor(optionsButton);
        stage.addActor(quitButton);

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
     * Metoda obsługująca skalowanie okna gry (libGDX).
     *
     * @param width  Szerokość okna.
     * @param height Wysokość okna.
     */
    @Override
    public void resize(int width, int height) {

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
        ChessLogo.getTexture().dispose();
    }
}
