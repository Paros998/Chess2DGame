package com.ourshipsgame.hud;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Klasa reprezentująca przycisk obrazkowy.
 * Dziedziczy po klasie ImageButton
 */
public class GameImageButton extends ImageButton {

    /**
     * Obiekty klasy Hud.
     * Jest referencją do elementów tej klasy.
     */
    private Hud hud;

    /**
     * Obiekt klasy optionsWindow.
     * Jest referencją do elementów tej klasy.
     */
    private OptionsWindow optionsWindow;

    // Game Menu constructors

    /**
     * Konstruktor klasy GameImageButton.
     *
     * @param x            Współrzędna osi X.
     * @param y            Współrzędna osi Y.
     * @param hud          Referencja do klasy Hud.
     * @param buttonStyles Style przycisków.
     */
    public GameImageButton(float x, float y, Hud hud, Sprite[] buttonStyles) {
        super(new SpriteDrawable(buttonStyles[0]),
                new SpriteDrawable(buttonStyles[1]));

        this.setX(x - this.getWidth());
        this.setY(y - this.getHeight());

        this.hud = hud;
        optionsWindow = new OptionsWindow("Game Menu", hud);
    }

    /**
     * Drugi konstruktor klasy GameImageButton.
     *
     * @param buttonStyles Style przycisków.
     */
    public GameImageButton(Sprite[] buttonStyles) {
        super(new SpriteDrawable(buttonStyles[0]),
                new SpriteDrawable(buttonStyles[1]));
    }

    public GameImageButton(Sprite[] buttonStyles, Hud hud) {
        super(new SpriteDrawable(buttonStyles[0]),
                new SpriteDrawable(buttonStyles[1]));

        this.hud = hud;
    }

    /**
     * Metoda ustawiająca słuchacza do przycisku.
     */
    public void setOptionsListener() {
        this.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hud.gameSettings.playSound();
                hud.gameEngineScreen.pause();
                optionsWindow.show(hud.getStage());
                optionsWindow.turnedOn = true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    public void setOptionsListener(String newPawn) {
        this.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hud.gameSettings.playSound();
                hud.gameEngineScreen.changePawn(newPawn);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }


    /**
     * Metoda typu get, zwraca w jakim stanie jest menu.
     *
     * @return Stan.
     */
    public boolean getGameMenuState() {
        return (optionsWindow != null && optionsWindow.turnedOn);
    }
}
