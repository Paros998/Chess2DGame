package com.ourshipsgame.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ourshipsgame.game.GameSlider;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Klasa reprezentująca okno dialogowe. Dziedziczy po klasie Dialog.
 */
public class OptionsWindow extends Dialog implements Constant {

    // Fields

    /**
     * Typ wyliczeniowy Actions, określa opcje w oknie dialogowym.
     */
    private enum Actions {
        RESUME_GAME, OPTIONS, BACK_TO_MAIN_MENU, SAVE;
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
    private final Hud hud;

    /**
     * Referencja "do siebie". Jest używany w konstruktorze podokna dialogowego przy
     * wyjściu z gry.
     */
    private final OptionsWindow backReference = this;

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
        super(windowName.toUpperCase(), hud.getSkin());
        this.hud = hud;
        turnedOn = false;
        layoutTable = new Table();
        layoutTable.center();
        layoutTable.setFillParent(true);

        this.button("RESUME GAME", Actions.RESUME_GAME);
        this.button("OPTIONS", Actions.OPTIONS);
        this.button("SAVE", Actions.SAVE);
        this.button("MAIN MENU", Actions.BACK_TO_MAIN_MENU);
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
            case RESUME_GAME -> {
                hud.gameSettings.playSound();
                hud.gameEngineScreen.resume();
                this.hide();
                turnedOn = false;
            }
            case OPTIONS -> {
                hud.gameSettings.playSound();
                new Dialog("OPTIONS", hud.getSkin()) {

                    {
                        soundSlider = new GameSlider(0, 100, 1, false, this.getSkin());
                        musicSlider = new GameSlider(0, 100, 1, false, this.getSkin());

                        musicSlider.setSliderType(1, hud.gameSettings);
                        soundSlider.setSliderType(2, hud.gameSettings);


                        this.row();
                        this.add("MUSIC VOLUME");
                        this.add("SFX VOLUME");
                        this.row();
                        this.add(musicSlider);
                        this.add(soundSlider);
                        this.row();
                        this.getCells().removeIndex(1);
                        this.add(this.getButtonTable());
                        this.button("BACK");
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
                hud.gameEngineScreen.resume();
            }
            case SAVE -> {
                if(hud.gameEngineScreen.gameStage != 4){
                    hud.gameSettings.playSound();
                    hud.gameEngineScreen.saveGame();
                }
                this.hide();
                turnedOn = false;
                hud.gameEngineScreen.resume();
            }
            case BACK_TO_MAIN_MENU -> {
                hud.gameSettings.playSound();
                new Dialog("CONFIRM EXIT", hud.getSkin()) {

                    {
                        button("YES", "Yes");
                        button("NO", "No");
                    }

                    @Override
                    protected void result(final Object act) {
                        if (Objects.equals(act.toString(), "Yes")) {
                            hud.game.menuElements = new MenuGlobalElements(hud.game);
                            hud.gameEngineScreen.dispose();
                            hud.gameSettings = null;
                            hud.gameEngineScreen = null;
                            hud.game.setScreen(new MenuScreen(hud.game));
                        } else {
                            hud.gameSettings.playSound();
                            backReference.show(hud.getStage());
                        }
                    }

                }.show(hud.getStage());
            }
        }
    }
}
