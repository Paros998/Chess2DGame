package com.ourshipsgame.mainmenu;

import java.text.NumberFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ourshipsgame.Main;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.hud.GameTextButton;
import com.ourshipsgame.mainmenu.Scores.Node;

/**
 * Klasa okna wyników w głównym menu.
 */
public class ScoreScreen implements Screen, Constant {

    /**
     * Okno przewijania (libGDX).
     * Zamieszczane są w nim wyniki z klasy Scores.
     */
    private ScrollPane scoreList;

    /**
     * Przycisk z libGDX. Wraca do głównego okna menu gry.
     */
    private GameTextButton backButton;

    /**
     * Tabela rozmieszczająca obiekty.
     * Ułatwia rozmieszczenie elementów na ekranie.
     */
    private Table layoutTable;

    /**
     * Tabela rozmieszczająca elementy w oknie przewijania.
     * Służy do prawidłowego rozmieszczenia danych w oknie.
     */
    private Table scrollTable;

    /**
     * Scena z silnika libGDX. 
     * Sprawia, że elementy w grze są interaktywne oraz rysuje je na ekranie.
     */
    private Stage stage;

    /**
     * Obiekt klasy score.
     * Wczytuje wyniki gracza z pliku.
     */
    private Scores scores;

    /**
     * Obiekt klasy Main. Odpowiedzialny głównie za zarządzanie ekranami. W tej
     * klasie jest również odwołaniem do klasy MenuGlobalElements.
     */
    private Main game;

    /**
     * Obiekt silnika libGDX. Rysuje obiekty na ekranie tj. sprite'y czy tekstury.
     */
    private SpriteBatch batch;

    /**
     * Czcionka do napisów.
     */
    private BitmapFont font;

    /**
     * Główny i jedyny konstruktor klasy ScoreScreen.
     * @param game Obiekt klasy Main.
     */
    public ScoreScreen(Main game) {
        this.game = game;
        scores = new Scores();
    }

    /**
     * Metoda odpowiedzialna za odświeżanie opreacji w oknie wyników.
     * @param deltaTime Główny czas silnika libGDX.
     */
    private void update(float deltaTime) {
        game.menuElements.moveMenu(deltaTime);
        stage.act();
    }

    /**
     * Metoda odopwiedzialna za tworzenie, ustawianie i ładowanie elementów w oknie
     * wyników (libGDX).
     */
    @Override
    public void show() {
        // // Creating batch and font
        batch = new SpriteBatch();
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("core/assets/fonts/Raleway-ExtraLightItalic.ttf"));
        parameter.color = Color.GOLD;
        parameter.borderColor = Color.BROWN;
        parameter.size = 25;
        parameter.borderWidth = 2;
        font = generator.generateFont(parameter);

        // Creating screen elements
        backButton = new GameTextButton("Back", 0, 0, game.menuElements.skin, 6, game, 1.3f);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Creating Layout Table
        layoutTable = new Table();
        layoutTable.center();
        layoutTable.setFillParent(true);

        // Creating scoreList
        scrollTable = new Table();
        String text;
        Node node;
        LabelStyle style = new LabelStyle(font, Color.GOLD);
        for (int i = 0; i < scores.scoresList.size(); i++) {
            node = scores.scoresList.get(i);
            text = (i + 1) + " - " + node.name + " Score: " + node.scoreValue + " Time: "
                    + String.format("%.2f", node.timeElapsed) + " Shots accuracy: "
                    + NumberFormat.getPercentInstance().format(node.accuracyRatio);
            Label rLabel = new Label(text, style);
            rLabel.setAlignment(Align.center);
            rLabel.setWrap(true);
            scrollTable.add(rLabel);
            scrollTable.row().pad(20);
        }

        scoreList = new ScrollPane(scrollTable, game.menuElements.skin);
        scoreList.setFillParent(true);
        scoreList.layout();

        // Adding elements to main Table
        scrollTable.add(backButton);
        layoutTable.add(scoreList).fill().expand();

        // Adding actors to stage
        stage.addActor(layoutTable);
    }

    /**
     * Metoda odpowiedzialna za renderowanie okna wyników (libGDX).
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
     * @param width Szerokość okna.
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
        batch.dispose();
        stage.dispose();
        font.dispose();
    }
}
