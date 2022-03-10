package com.ourshipsgame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ourshipsgame.Main;
import com.ourshipsgame.game.GameScreen;
import com.ourshipsgame.mainmenu.HelpScreen;
import com.ourshipsgame.mainmenu.MenuScreen;
import com.ourshipsgame.mainmenu.OptionScreen;
import com.ourshipsgame.mainmenu.ScoreScreen;

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
     * @param nameTag Tekst wyświetlany na przycisku.
     * @param skin Skórka do przycisku.
     * @param buttonNumber Numer przycisku (do metody menuOptions).
     */
    public GameTextButton(String nameTag, Skin skin, final int buttonNumber) {
        super(nameTag, skin);
    }

    // While in Main Menu Constructor

    /**
     * Drugi konstruktor klasy GameTextButton. Używany w głównym menu.
     * @param nameTag Tekst wyświetlany na przycisku.
     * @param x Pozycja w X.
     * @param y Pozycja w Y.
     * @param skin Skórka do przycisku.
     * @param buttonNumber Numer przycisku (do metody menuOptions).
     * @param game Referencja obiektu Main.
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
     * @param option Numer przycisku.
     */
    private void menuOptions(int option) {
        switch (option) {
        case 1: // Enters to the game
            game.menuElements.disposeMenu();
            game.setScreen(new GameScreen(game));
            break;

        case 2: // Enters to a help screen
            game.setScreen(new HelpScreen(game));
            break;

        case 3: // Enters to a scores screen
            game.setScreen(new ScoreScreen(game));
            break;

        case 4: // Enters to a settings screen
            game.setScreen(new OptionScreen(game));
            break;

        case 5: // Exits game
            Gdx.app.exit();
            break;

        case 6: // Backs from current screen
            game.setScreen(new MenuScreen(game));
            break;
        }
    }

}
