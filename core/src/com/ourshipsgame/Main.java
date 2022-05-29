package com.ourshipsgame;

import com.badlogic.gdx.Game;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;

/**
 * Główna klasa operująca oknami gry.
 */
public class Main extends Game {

    /**
     * Obiekt klasy MenuGlobalElements.
     * Przechowuje elementy, które są identyczne dla okien w menu gry.
     */
    public MenuGlobalElements menuElements;

    /**
     * Metoda odpowiedzialna za stworzenie okna gry (libGDX).
     */
    @Override
    public void create() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        menuElements = new MenuGlobalElements(this);
        setScreen(new MenuScreen(this));
    }

    /**
     * Metoda obsługująca niszczenie elementów silnika libGDX.
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}