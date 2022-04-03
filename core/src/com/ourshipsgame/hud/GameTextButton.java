package com.ourshipsgame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ourshipsgame.Main;
import com.ourshipsgame.game.MultiPlayerGameScreen;
import com.ourshipsgame.game.SinglePlayerGameScreen;
import com.ourshipsgame.mainmenu.*;

/**
 * Klasa reprezentująca przycisk tekstowy.
 * Dziedziczy po klasie TextButton
 */
public class GameTextButton extends TextButton {

    /**
     * Referencja do motywu przycisków oraz innych elementów gui w grze.
     */
    public Skin skin;

    /**
     * Obiekt klasy Main.
     * Odpowiedzialny głównie za zarządzanie ekranami.
     */
    Main game;

    // While in Game Constructor

    /**
     * Konstruktor klasy GameTextButton. Używany podczas rozgrywki.
     *
     * @param nameTag      Tekst wyświetlany na przycisku.
     * @param skin         Skórka do przycisku.
     * @param buttonNumber Numer przycisku (do metody menuOptions).
     */
    public GameTextButton(String nameTag, Skin skin, final int buttonNumber) {
        super(nameTag, skin);
    }

    // While in Main Menu Constructor

    /**
     * Drugi konstruktor klasy GameTextButton. Używany w głównym menu.
     *
     * @param nameTag      Tekst wyświetlany na przycisku.
     * @param x            Pozycja w X.
     * @param y            Pozycja w Y.
     * @param skin         Skórka do przycisku.
     * @param buttonNumber Numer przycisku (do metody menuOptions).
     * @param game         Referencja obiektu Main.
     */
    public GameTextButton(String nameTag, float x, float y, Skin skin, final int buttonNumber, final Main game) {
        super(nameTag, skin);
        this.game = game;
        this.setX(x - this.getWidth() / 2);
        this.setY(y);

        this.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                menuOptions(buttonNumber);
                game.menuElements.gameSettings.clickSound.play(game.menuElements.gameSettings.soundVolume);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    /**
     * Metoda określa numer przycisku i na jego podstawie przełączany jest ekran.
     *
     * @param option Numer przycisku.
     */
    private void menuOptions(int option) {
        switch (option) {
            case 1 -> game.setScreen(new SelectGameModeScreen(game));
            case 2 -> game.setScreen(new HelpScreen(game));
            case 3 -> game.setScreen(new ScoreScreen(game));
            case 4 -> game.setScreen(new OptionScreen(game));
            case 5 -> Gdx.app.exit();
            case 6 -> game.setScreen(new MenuScreen(game));
            // Game mode selection screen
            case 7 -> {
                game.menuElements.disposeMenu();
                game.setScreen(new SinglePlayerGameScreen(game));
            }
            case 8 -> {
                // Load existing game
            }
            case 9 -> {
                game.menuElements.disposeMenu();
                game.setScreen(new MultiPlayerGameScreen(game));
            }
        }
    }
}
