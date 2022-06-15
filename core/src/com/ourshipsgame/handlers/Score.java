package com.ourshipsgame.handlers;

import java.text.NumberFormat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Klasa przechowująca wyniki gracza i komputera w czasie bitwy
 */
//TODO do usunięcia
public class Score {
    /**
     * Zmienna przechowująca wynik
     */
    private float scoreValue;
    /**
     * Zmienna przechowująca czas trwania tur
     */
    private float timeElapsed;
    /**
     * Zmienna przechowująca ilość strzałów oddanych
     */
    private int shotsFired;
    /**
     * Zmienna przechowująca ilość niecelnych strzałów
     */
    private int shotsMissed;
    /**
     * Zmienna przechowująca ilość celnych strzałów
     */
    private int shotsHitted;
    /**
     * Zmienna przechowująca procentową celność trafień
     */
    private float accuracyRatio;
    /**
     * Zmienna przechowująca ilość zniszczonych statków
     */
    private int shipsDestroyed;
    /**
     * Zmienna określająca identyfikator obiektu
     */
    private int idNumber;
    /**
     * Zmienna przechowująca nazwę
     */
    private String PlayerName;
    /**
     * Zmienna przechowująca ilość trafień bez przerwy
     */
    private int combo;

    /**
     * Konstruktor obiektu klasy Score nadający identyfikator
     * 
     * @param id Identyfikator
     */
    public Score(int id) {
        idNumber = id;
    }

    /**
     * Metoda ustawiająca nazwę do obiektu
     * 
     * @param name Nazwa gracza lub komputer
     */
    public void setPlayerName(String name) {
        PlayerName = name;
    }

    /**
     * Metoda do rysowania na ekranie elementów informacyjnych przechowywwanych w
     * obiekcie
     * 
     * @param hudFont        Czcionka do tekstu
     * @param batch          SpriteBatch do rysowania na ekranie
     * @param gameWidth_f    Szerokość okna gry w pikselach
     * @param gameHeight_f   Wysokość okna gry w pikselach
     * @param ThreeShipsLeft Ilość niezniszczonych statków trój-polowych
     * @param TwoShipsLeft   Ilość niezniszczonych statków dwu-polowych
     * @param OneShipsLeft   Ilość niezniszczonych statków jedno-polowych
     * @param shipIcons      Tablica tekstur do ikonek statków
     */
    public void drawInfo(BitmapFont hudFont, SpriteBatch batch, float gameWidth_f, float gameHeight_f,
            int ThreeShipsLeft, int TwoShipsLeft, int OneShipsLeft, Texture[] shipIcons) {
        float xstart;
        float vFloat;
        int second;
        String time;
        switch (idNumber) {
        case 1:

            xstart = gameWidth_f / 2 - 300;

            hudFont.draw(batch, this.getPlayerName(), xstart, gameHeight_f - 10);

            vFloat = this.getTimeElapsed();
            second = (int) vFloat / 60;
            time = "Total time: " + second + "." + String.format("%.2f", vFloat - second * 60f);
            hudFont.draw(batch, time, xstart, gameHeight_f - 35);

            hudFont.draw(batch, "Accuracy: " + NumberFormat.getPercentInstance().format(accuracyRatio), xstart + 150,
                    gameHeight_f - 35);

            hudFont.draw(batch, "Total score: " + this.getScoreValue(), xstart, gameHeight_f - 60);

            hudFont.draw(batch, "Combo: " + this.getCombo(), xstart + 150, gameHeight_f - 60);

            batch.draw(shipIcons[0], xstart, gameHeight_f - 110);
            hudFont.draw(batch, ThreeShipsLeft + "/3", xstart + shipIcons[0].getWidth(), gameHeight_f - 85);

            batch.draw(shipIcons[1], xstart + 50, gameHeight_f - 107.5f);
            hudFont.draw(batch, TwoShipsLeft + "/4", xstart + 50 + shipIcons[1].getWidth(), gameHeight_f - 85);

            batch.draw(shipIcons[2], xstart + 100, gameHeight_f - 105);
            hudFont.draw(batch, OneShipsLeft + "/5", xstart + 100 + shipIcons[2].getWidth(), gameHeight_f - 85);
            break;
        case 2:

            xstart = gameWidth_f / 2 + 170;

            hudFont.draw(batch, this.getPlayerName(), xstart, gameHeight_f - 10);

            vFloat = this.getTimeElapsed();
            second = (int) vFloat / 60;
            time = "Total time: " + second + "." + String.format("%.2f", vFloat - second * 60f);
            hudFont.draw(batch, time, xstart, gameHeight_f - 35);

            hudFont.draw(batch, "Accuracy: " + NumberFormat.getPercentInstance().format(accuracyRatio), xstart - 150,
                    gameHeight_f - 35);

            hudFont.draw(batch, "Total score: " + this.getScoreValue(), xstart, gameHeight_f - 60);

            hudFont.draw(batch, "Combo: " + this.getCombo(), xstart - 150, gameHeight_f - 60);

            batch.draw(shipIcons[0], xstart - 5, gameHeight_f - 110);
            hudFont.draw(batch, ThreeShipsLeft + "/3", xstart + shipIcons[0].getWidth(), gameHeight_f - 85);

            batch.draw(shipIcons[1], xstart + 50 - 5, gameHeight_f - 107.5f);
            hudFont.draw(batch, TwoShipsLeft + "/4", xstart + 50 + shipIcons[1].getWidth(), gameHeight_f - 85);

            batch.draw(shipIcons[2], xstart + 100 - 5, gameHeight_f - 105);
            hudFont.draw(batch, OneShipsLeft + "/5", xstart + 100 + shipIcons[2].getWidth(), gameHeight_f - 85);
            break;
        }
    }

    /**
     * Metoda do aktualizacji danych odnośnie strzałów
     * 
     * @param PlayerShots Tablica strzałów właściciela tego obiektu
     */
    public void update(int[][] PlayerShots) {
        shotsFired = shotsHitted = shotsMissed = 0;
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                if (PlayerShots[i][j] != 0)
                    shotsFired++;
                if (PlayerShots[i][j] == -1)
                    shotsMissed++;
                else if (PlayerShots[i][j] == 1 || PlayerShots[i][j] == 2)
                    shotsHitted++;
            }
        if (shotsFired != 0)
            accuracyRatio = (float) shotsHitted / (float) shotsFired;
    }

    /**
     * Metoda aktualizująca czas trwania tur właściciela tego obiektu
     * 
     * @param deltaTime Czas między klatkami gry
     */
    public void updateTime(float deltaTime) {
        timeElapsed += deltaTime;
    }

    /**
     * Metoda przyznająca punkty za trafienie
     */
    public void addPointsForHit() {
        this.scoreValue += (50 * (1 + 0.1 * combo));
    }

    /**
     * Metoda przyznająca punkty za zniszczenie
     * 
     * @param sizeofShip Wielkość zniszczonego statku
     */
    public void addPointsForDestroy(int sizeofShip) {
        shipsDestroyed++;
        if (sizeofShip == 3)
            this.scoreValue += (150 * (1 + 0.05 * combo));
        else if (sizeofShip == 2)
            this.scoreValue += (100 * (1 + 0.05 * combo));
        else
            this.scoreValue += (50 * (1 + 0.05 * combo));
    }

    /**
     * Metoda zwiększająca combo
     */
    public void increaseCombo() {
        this.combo++;
    }

    /**
     * Metoda zerująca combo
     */
    public void zeroCombo() {
        this.combo = 0;
    }

    /**
     * Metoda zwracająca wynik właściciela tego obiektu
     * 
     * @return the scoreValue
     */
    public float getScoreValue() {
        return scoreValue;
    }

    /**
     * Metoda zwracająca nazwę właściciela tego obiektu
     * 
     * @return the playerName
     */
    public String getPlayerName() {
        return PlayerName;
    }

    /**
     * Metoda zwracająca czas trwania tur właściciela tego obiektu
     * 
     * @return the timeElapsed
     */
    public float getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * Metoda zwracająca celność właściciela tego obiektu
     * 
     * @return the accuracyRatio
     */
    public float getAccuracyRatio() {
        return accuracyRatio;
    }

    /**
     * Metoda zwracająca combo właściciela tego obiektu
     * 
     * @return the combo
     */
    public int getCombo() {
        return combo;
    }

    /**
     * Metoda zwracająca ilość zniszczonych statków przez właściciela tego obiektu
     * 
     * @return the shipsDestroyed
     */
    public int getShipsDestroyed() {
        return shipsDestroyed;
    }

    /**
     * Metoda zwracająca ilość niecelnych strzałów właściciela tego obiektu
     * 
     * @return the shotsMissed
     */
    public int getShotsMissed() {
        return shotsMissed;
    }

    /**
     * Metoda zwracająca identyfikator właściciela tego obiektu
     * 
     * @return the idNumber
     */
    public int getIdNumber() {
        return idNumber;
    }
}
