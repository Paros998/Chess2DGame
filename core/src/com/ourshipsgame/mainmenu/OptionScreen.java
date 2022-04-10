package com.ourshipsgame.mainmenu;

import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ourshipsgame.Main;
import com.ourshipsgame.game.GameSlider;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.hud.GameTextButton;

/**
 * Klasa okna opcji w głównym menu.
 */
public class OptionScreen implements Screen, Constant {

    // Class Fields

    /**
     * Obiekt klasy Main. Odpowiedzialny głównie za zarządzanie ekranami. W tej
     * klasie jest również odwołaniem do klasy MenuGlobalElements.
     */
    private Main game;

    /**
     * Ssuwak z libGDX. Służy do regulowania głośności muzyki.
     */
    private GameSlider musicSliderVolume;

    /**
     * Ssuwak z libGDX. Służy do regulowania głośności dźwięków w grze.
     */
    private GameSlider soundSilderVolume;

    /**
     * Przycisk z libGDX. Wraca do głównego okna menu gry.
     */
    private GameTextButton backButton;

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
     * Pole tekstowe z libGDX. Opisuje poniższy ssuwak w oknie.
     */
    public TextField soundsVolumeText;

    /**
     * Pole tekstowe z libGDX. Opisuje poniższy ssuwak w oknie.
     */
    public TextField musicVolumeText;

    // Constructor

    /**
     * Główny i jedyny konstruktor klasy OptionScreen.
     * 
     * @param game Obiekt klasy Main.
     */
    public OptionScreen(Main game) {
        this.game = game;
    }

    // Methods

    /**
     * Metoda odopwiedzialna za tworzenie, ustawianie i ładowanie elementów w oknie
     * opcji (libGDX).
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        float scale = 1.3f;

        // Texts
        soundsVolumeText = new TextField("SFX Volume", game.menuElements.skin);
        soundsVolumeText.clearListeners();
        soundsVolumeText.setWidth(soundsVolumeText.getWidth() * scale);
        soundsVolumeText.setAlignment(1);
        soundsVolumeText.setPosition(GAME_WIDTH_F / 2 - soundsVolumeText.getWidth() / 2, GAME_HEIGHT_F / 2 + 180);

        musicVolumeText = new TextField("Music Volume", game.menuElements.skin);
        musicVolumeText.clearListeners();
        musicVolumeText.setWidth(musicVolumeText.getWidth() * scale);
        musicVolumeText.setAlignment(1);
        musicVolumeText.setPosition(GAME_WIDTH_F / 2 - soundsVolumeText.getWidth() / 2, GAME_HEIGHT_F / 2 - 20);

        // Sliders
        musicSliderVolume = new GameSlider(GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2 - 100, 0, 100, 1, false,
                game.menuElements.skin, game, scale);
        musicSliderVolume.setSliderType(1);

        soundSilderVolume = new GameSlider(GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2 + 100, 0, 100, 1, false,
                game.menuElements.skin, game, scale);
        soundSilderVolume.setSliderType(2);

        // Buttons
        backButton = new GameTextButton("Back", GAME_WIDTH_F / 2, GAME_HEIGHT_F / 2 - 300, game.menuElements.skin, 6, game, scale);

        stage.addActor(backButton);
        stage.addActor(musicSliderVolume);
        stage.addActor(soundSilderVolume);
        stage.addActor(soundsVolumeText);
        stage.addActor(musicVolumeText);
    }

    /**
     * Metoda odpowiedzialna za odświeżanie opreacji w oknie opcji.
     * 
     * @param deltaTime Główny czas silnika libGDX.
     */
    private void update(float deltaTime) {
        game.menuElements.moveMenu(deltaTime);
        musicSliderVolume.setVisualPercent(game.menuElements.gameSettings.sliderMusicPercent);
        soundSilderVolume.setVisualPercent(game.menuElements.gameSettings.sliderSoundPercent);
        stage.act();
    }

    /**
     * Metoda odpowiedzialna za zapis ustawień z okna opcji do pliku tekstowego
     * settings.txt.
     * 
     * @throws IOException Wyjątek związany z plikami.
     */
    private void saveSettings() throws IOException {
        FileWriter savingPrintWriter;
        savingPrintWriter = new FileWriter("settings.txt", false);
        savingPrintWriter.write(musicSliderVolume.getPercent() + "\n" + soundSilderVolume.getPercent());
        savingPrintWriter.close();
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
        try {
            saveSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.dispose();
        batch.dispose();
    }
}
