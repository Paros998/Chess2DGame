package com.ourshipsgame.hud;

import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ourshipsgame.game.GameSlider;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;

/**
 * Klasa reprezentująca okno dialogowe. Dziedziczy po klasie Dialog.
 */
public class OptionsWindow extends Dialog implements Constant {

    // Fields

    /**
     * Typ wyliczeniowy Actions, określa opcje w oknie dialogowym.
     */
    private enum Actions {
        RESUME_GAME, OPTIONS, BACK_TO_MAIN_MENU;
    }

    /**
     * Tablica rozmieszczenia elementów w oknie dialogowym.
     */
    private Table layoutTable;

    /**
     * Określa czy okno dialogowe się pojawiło.
     */
    public boolean turnedOn;

    /**
     * Referencja do elementów w klasie Hud.
     */
    private Hud hud;

    /**
     * Referencja "do siebie". Jest używany w konstruktorze podokna dialogowego przy
     * wyjściu z gry.
     */
    private OptionsWindow backReference = this;

    /**
     * Ssuwak z libGDX. Służy do regulowania głośności dźwięków.
     */
    private GameSlider soundSlider;

    /**
     * Ssuwak z libGDX. Służy do regulowania głośności muzyki.
     */
    private GameSlider musicSlider;

    // Constructor

    /**
     * Główny i jedyny konstruktor klasy OptionsWindow.
     * 
     * @param windowName Nazwa okna.
     * @param hud        Referencja obiektu Hud.
     */
    public OptionsWindow(String windowName, Hud hud) {
        super(windowName, hud.getSkin());
        this.hud = hud;
        turnedOn = false;
        layoutTable = new Table();
        layoutTable.center();
        layoutTable.setFillParent(true);

        this.button("Resume Game", Actions.RESUME_GAME);
        this.button("Options", Actions.OPTIONS);
        this.button("Back to Main Menu", Actions.BACK_TO_MAIN_MENU);
        layoutTable.add(this).expandX().padBottom(10);
    }

    // Methods

    /**
     * Metoda zapisująca ustawienia gry do pliku.
     * 
     * @throws IOException Wyjątek związany z plikiem.
     */
    private void saveSettings() throws IOException {
        FileWriter savingPrintWriter;
        savingPrintWriter = new FileWriter("settings.txt", false);
        savingPrintWriter.write(musicSlider.getPercent() + "\n" + soundSlider.getPercent());
        savingPrintWriter.close();
    }

    /**
     * Metoda wynikowa po wciśnięciu przycisku w oknie dialogowym.
     * 
     * @param act Obiekt przycisku.
     */
    @Override
    protected void result(final Object act) {
        Actions action = Actions.valueOf(act.toString());
        switch (action) {
            case RESUME_GAME:
                hud.gameSettings.playSound();
                this.hide();
                turnedOn = false;
                break;

            case OPTIONS:
                hud.gameSettings.playSound();
                new Dialog("Options", hud.getSkin()) {

                    {
                        soundSlider = new GameSlider(0, 100, 1, false, this.getSkin());
                        musicSlider = new GameSlider(0, 100, 1, false, this.getSkin());

                        musicSlider.setSliderType(1, hud.gameSettings);
                        soundSlider.setSliderType(2, hud.gameSettings);

                        this.text("Music Volume");
                        this.row();
                        this.add(musicSlider).expandX().padLeft(20);
                        this.row();
                        this.text("SFX Volume");
                        this.add(soundSlider).expandX().padLeft(20);
                        this.row();
                        this.getCells().removeIndex(1);
                        this.add(this.getButtonTable());
                        this.button("Back");
                    }

                    @Override
                    protected void result(final Object act) {
                        hud.gameSettings.playSound();

                        try {
                            saveSettings();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        backReference.show(hud.getStage());
                    }

                }.show(hud.getStage());
                break;

            case BACK_TO_MAIN_MENU:
                hud.gameSettings.playSound();
                new Dialog("Confitm Exit", hud.getSkin()) {

                    {
                        button("Yes", "Yes");
                        button("No", "No");
                    }

                    @Override
                    protected void result(final Object act) {
                        if (act.toString() == "Yes") {
                            hud.game.menuElements = new MenuGlobalElements(hud.game);
                            hud.gameScreen.dispose();
                            hud.gameSettings = null;
                            hud.gameScreen = null;
                            hud.game.setScreen(new MenuScreen(hud.game));
                        } else {
                            hud.gameSettings.playSound();
                            backReference.show(hud.getStage());
                        }
                    }

                }.show(hud.getStage());
                break;
        }
    }
}
