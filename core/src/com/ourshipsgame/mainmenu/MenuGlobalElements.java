package com.ourshipsgame.mainmenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ourshipsgame.game.GameObject;
import com.ourshipsgame.game.GameSettings;

/**
 * Klasa zawierająca metody i pola wykorzystywane przez okna menu.
 */
public class MenuGlobalElements {

    /**
     * Obiekt klasy Main. Odpowiedzialny głównie za zarządzanie ekranami. W tej
     * klasie jest również odwołaniem do klasy MenuGlobalElements.
     */
    public Game game;

    /**
     * Obiekt klasy GameSettings.
     * Zawiera ustawienia dźwięków i muzyki w grze.
     */
    public GameSettings gameSettings;

    /**
     * Modyfikowalny napis z libGDX, tytuł gry.
     */
    public GlyphLayout layout;

    /**
     * Czcionka do napisów.
     */
    public BitmapFont font;

    /**
     * Obiekt klasy Pixmap wyszukujący ścieżkę pliku kursora myszki.
     */
    public Pixmap pixmap;

    /**
     * Kursor myszki.
     */
    public Cursor cursor;

    /**
     * Tło w głównym menu gry.
     */
    public GameObject menuTexture;

    /**
     * Motyw przycisków oraz innych elementów gui w grze.
     */
    public Skin skin;

    /**
     * Kierunek poruszania się tła.
     */
    private int direction = 0;

    /**
     * Główny i jedyny konstruktor klasy MenuGlobalElements.
     * @param game Obiekt klasy Game.
     */
    public MenuGlobalElements(Game game) {

        this.game = game;
        gameSettings = new GameSettings(game);
        menuTexture = new GameObject(new Texture("core/assets/backgroundtextures/ChessMenuBgv2.png"), 0, 0, true, false,
                null);
        skin = new Skin(Gdx.files.internal("core/assets/buttons/skins/golden-spiral/skin/golden-ui-skin.json"));

        // Text, font
        font = new BitmapFont(Gdx.files.internal("core/assets/buttons/skins/golden-spiral/raw/font-title-export.fnt"));
        layout = new GlyphLayout();
        layout.setText(font, "Chess Game");


        // Cursor
        pixmap = new Pixmap(Gdx.files.internal("core/assets/ui/ui.hud/cursors/test.png"));
        cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursor);
    }

    
    /** 
     * Metoda odpowiedzialna za ruch tła w głównym menu gry.
     * @param deltaTime Główny czas silnika libGDX.
     */
    // Menu methods
    public void moveMenu(float deltaTime) {
        if ((menuTexture.x <= 0) && (direction == 0)) {
            if (menuTexture.x <= -119)
                direction = 1;
            menuTexture.moveTexture(-20 * deltaTime);
        }

        if ((menuTexture.x >= -120) && (direction == 1)) {
            if (menuTexture.x >= -1)
                direction = 0;
            menuTexture.moveTexture(20 * deltaTime);
        }
    }

    /**
     * Metoda odpowiedzialna za zniszczenie elementów menu po kliknięciu Play.
     */
    public void disposeMenu() {
        font.dispose();
        skin.dispose();
        pixmap.dispose();
        menuTexture.getTexture().dispose();
    }
}
