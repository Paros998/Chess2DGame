package com.ourshipsgame.inteligentSystems;

import com.ourshipsgame.game.Player;

/**
 * Klasa odpowiadająca za podejmowanie decyzji przez komputer
 */
public class ComputerPlayerAi {

    private Player player;
    /**
     * Metoda do aktualizowania danych i logiki komputera
     * 

     */
    public void update() {

    }

    /**
     * Konstruktor główny obiektu, który inicjuje i ustawia dane do obliczeń logiki
     * 

     */
    public ComputerPlayerAi() {
    }

    public void setPlayer(Player myPlayer) {
        this.player = myPlayer;
    }

    public Player getPlayer() {
        return player;
    }
}
