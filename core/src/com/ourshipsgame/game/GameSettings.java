package com.ourshipsgame.game;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Klasa zawierająca ustawienia gry.
 */
public class GameSettings {

    /**
     * Referencja obiektu Game.
     */
    public Game game;

    /**
     * Muzyka w grze.
     */
    public Music music;

    /**
     * Dzwięk kliknięcia.
     */
    public Sound clickSound;

    /**
     * Wartość ssuwaka od muzyki.
     */
    public float sliderMusicPercent;

    /**
     * Wartość ssuwaka od dźwięków.
     */
    public float sliderSoundPercent;

    /**
     * Wartość głośności dźwięków.
     */
    public float soundVolume;

    /**
     * Wartość głośności muzyki.
     */
    public float musicVolume;

    /**
     * Konstruktor klasy GameSettings.
     * 
     * @param game Referencja obiektu Game.
     */
    public GameSettings(Game game) {
        this.game = game;

        try {
            loadSettings();
        } /* loading settings from file */
        catch (IOException e) {
            e.printStackTrace();
        }
        // Music and sounds
        music = Gdx.audio.newMusic(Gdx.files.internal("core/assets/music/Blackmoor Tides Loop.wav"));
        music.setLooping(true);
        music.setVolume(musicVolume);
        music.play();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("core/assets/sounds/menu-click.mp3"));
        clickSound.pause();
    }

    /**
     * Metoda wczytująca ustawienia z pliku.
     * 
     * @throws IOException Wyjątek związany z plikiem.
     */
    private void loadSettings() throws IOException {
        File file = new File("settings.txt");
        if (!file.exists()) {
            file.createNewFile(); // Creating player file with home details
            musicVolume = 0.2f;
            soundVolume = 0.2f;
            sliderSoundPercent = sliderMusicPercent = 100.0f;
        } else {
            Scanner scanner = new Scanner(file);

            sliderMusicPercent = Float.valueOf(scanner.nextLine().trim());
            sliderSoundPercent = Float.valueOf(scanner.next().trim());

            musicVolume = sliderMusicPercent / 5.0f;
            soundVolume = sliderSoundPercent / 5.0f;

            scanner.close();
        }
    }

    /**
     * Metoda włączająca dźwięk kliknięcia
     */
    public void playSound() {
        clickSound.play(soundVolume);
    }

    /**
     * Metoda usuwająca wszystkie elementy
     */
    public void dispose() {
        music.dispose();
        clickSound.dispose();
    }
}
