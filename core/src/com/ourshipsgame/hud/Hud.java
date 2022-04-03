package com.ourshipsgame.hud;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ourshipsgame.Main;
import com.ourshipsgame.game.GameEngine;
import com.ourshipsgame.game.SinglePlayerGameScreen;
import com.ourshipsgame.game.GameSettings;
import com.ourshipsgame.handlers.Constant;

/**
 * Klasa hud'u (heads-up display) gry.
 */
public class Hud implements Constant {

    // Fields

    /**
     * Główny przycisk do opcji gry w trakcie rozgrywki.
     * Jest to złote koło zębate w górnym prawym rogu ekranu.
     */
    private GameImageButton gameMenuButton;

    /**
     * Przycisk do generowania losowego ustawienia statków na planszy.
     */
    private GameImageButton repeatButton;

    /**
     * Przycisk zatwierdzenia ustawienia statków.
     * Gdy świeci na czerwono nie można przejść do rozgrywki po jego naciśnięciu.
     * Gdy świeci na zielono można przejść do rozgrywki po jego naciśnięciu.
     */
    private GameImageButton playButton;

    /**
     * Style przycisku startu.
     * Używane tylko przy tworzeniu przycisku.
     */
    private Sprite playButtonGreenStyle;

    /**
     * Pole tekstowe do wpisywania nazwy gracza.
     */
    private TextField playerNameTextField;

    /**
     * Okno dialogowe do ustawienia nazwy gracza.
     */
    private Dialog playersSetNameDialog;

    /**
     * Nazwa gracza przekazywana do GameScreen.
     */
    private String playersName;

    /**
     * Tabela do rozmieszczenia elementów na ekranie.
     */
    public Table layoutTable;

    /**
     * Scena z silnika libGDX. Sprawia, że elementy w grze są interaktywne oraz
     * rysuje je na ekranie.
     */
    private Stage stage;

    /**
     * Motyw przycisków oraz innych elementów gui w grze.
     */
    private Skin skin;

    /**
     * Obiekt klasy GameSettings.
     * Zawiera ustawienia dźwięków i muzyki w grze.
     */
    public GameSettings gameSettings;

    /**
     * Obiekt klasy GameScreen. 
     * W tej klasie odpowiedzialny za niszczenie elementów Hud.
     */
    public GameEngine gameEngineScreen;

    /**
     * Obiekt klasy Main. 
     * Odpowiedzialny głównie za zarządzanie ekranami.
     */
    public Main game;

    /**
     * Kursor myszki.
     */
    public Cursor cursor;

    /**
     * Główny i jedyny konstruktor klasy Hud.
     * @param manager Pobierane są z niego tekstury.
     * @param game Przełącza ekrany. Powrót do menu.
     * @param gameEngineScreen Niszczy elementy Hud.
     * @param kCursor Referencja do kursora myszki.
     */
    // Constructor
    public Hud(AssetManager manager, Main game, GameEngine gameEngineScreen, Cursor kCursor) {
        skin = new Skin();
        skin = manager.get("core/assets/buttons/skins/golden-spiral/skin/golden-ui-skin.json", Skin.class);
        cursor = kCursor;
        stage = new Stage(new ScreenViewport());

        this.game = game;
        this.gameEngineScreen = gameEngineScreen;

        // Close button
        Texture[] buttonStyles = new Texture[2];
        buttonStyles[0] = manager.get("core/assets/ui/ui.hud/ui/global/modern/gear.png", Texture.class);
        buttonStyles[1] = manager.get("core/assets/ui/ui.hud/ui/global/modern/gear-press.png", Texture.class);

        Sprite[] buttonStylesSprites = new Sprite[2];

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.55f);

        gameMenuButton = new GameImageButton(GAME_WIDTH - 10, GAME_HEIGHT - 3, this, buttonStylesSprites);
        gameMenuButton.setOptionsListener();

        // Repeat button
        buttonStyles[0] = manager.get("core/assets/ui/reverse-button.png", Texture.class);
        buttonStyles[1] = manager.get("core/assets/ui/reverse-button-pressed.png", Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 6.5f);

        repeatButton = new GameImageButton(buttonStylesSprites);

        // Play button
        buttonStyles[0] = manager.get("core/assets/ui/ready-button.png", Texture.class);
        buttonStyles[1] = manager.get("core/assets/ui/ready-button-pressed.png", Texture.class);
        playButtonGreenStyle = new Sprite(manager.get("core/assets/ui/ready-button-go.png", Texture.class));

        setButtonsSprites(buttonStyles, buttonStylesSprites, 6.5f);

        playButton = new GameImageButton(buttonStylesSprites);

        // Player Name textfield
        playersName = "Player";
        playersSetNameDialog = new Dialog("Enter Your Name", skin) {

            {
                playerNameTextField = new TextField(playersName, skin);
                this.text("");
                this.row();
                this.add(playerNameTextField);
                this.row();
                this.getCells().removeIndex(1);
                this.add(this.getButtonTable());
                this.button("Accept");
            }

            @Override
            protected void result(final Object act) {
                playersName = playerNameTextField.getText();
                playerNameTextField = null;
            }
        };

        playersSetNameDialog.show(stage).setPosition(GAME_WIDTH / 2 + 150, 150);

        // Table for play, repeat buttons
        layoutTable = new Table();
        layoutTable.bottom();
        layoutTable.setFillParent(true);
        layoutTable.add(playButton).expandX().padLeft(830f).padBottom(8);
        layoutTable.add(repeatButton).expandX().padRight(830f).padBottom(8);

        stage.addActor(gameMenuButton);
        stage.addActor(layoutTable);
    }

    // Methods

    /**
     * Metoda ustawiająca sprite'y przycisków.
     * @param textures Tablica tekstur.
     * @param sprites Tablica sprite'ów.
     * @param factor Współczynnik rozmiaru.
     */
    private void setButtonsSprites(Texture[] textures, Sprite[] sprites, float factor) {
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(textures[i]);
            sprites[i].setSize(sprites[i].getWidth() / factor, sprites[i].getHeight() / factor);
        }
    }

    /**
     * Metoda aktualizująca w czasie gry elementy hud'u.
     */
    public void update() {
        stage.act();
        stage.draw();
    }

    /**
     * Metoda niszcząca elementy hud'u.
     */
    public void dispose() {
        stage.dispose();
        skin.dispose();
        cursor.dispose();
    }

    /**
     * Metoda typu get, zwraca przycisk.
     * @return Przycisk do generowania losowego ustawienia statków.
     */
    // Getters
    public GameImageButton getRepeatButton() {
        return repeatButton;
    }

    /**
     * Metoda typu get, zwraca przycisk.
     * @return Przycisk do przejścia do rozgrywki.
     */
    public GameImageButton getPlayButton() {
        return playButton;
    }

    /**
     * Metoda typu get, zwraca String.
     * @return Nazwa gracza.
     */
    public String getPlayersName() {
        return playersName;
    }

    /**
     * Metoda typu get, zwraca Sprite.
     * @return Styl przycisku Play.
     */
    public Sprite getPlayButtonGreenStyle() {
        return playButtonGreenStyle;
    }

    /**
     * Metoda typu get, zwraca okno dialogowe.
     * @return Okno dialogowe.
     */
    public Dialog getPlayersSetNameDialog() {
        return playersSetNameDialog;
    }

    /**
     * Metoda typu get, zwraca Stage.
     * @return Scena elementów hud.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Metoda typu get, zwraca Skin.
     * @return Skórka elementów gry.
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Metoda typu get, zwraca boolean.
     * @return Czy włączone są opcje gry.
     */
    public boolean isPasued() {
        return gameMenuButton.getGameMenuState();
    }
}
