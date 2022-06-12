package com.ourshipsgame.hud;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ourshipsgame.Main;
import com.ourshipsgame.game.GameEngine;
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
    private Dialog playersSetNameDialog = null;

    /**
     * Okno dialogowe do ustawienia nowego pionka
     */
    public final Dialog pawnChangeDialog;

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

    public final AssetManager manager;


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
     *
     * @param manager          Pobierane są z niego tekstury.
     * @param game             Przełącza ekrany. Powrót do menu.
     * @param gameEngineScreen Niszczy elementy Hud.
     * @param kCursor          Referencja do kursora myszki.
     */

    public Hud(AssetManager manager, Main game, GameEngine gameEngineScreen, Cursor kCursor) {
        skin = new Skin();
        skin = manager.get("core/assets/buttons/skins/golden-spiral/skin/golden-ui-skin.json", Skin.class);
        cursor = kCursor;
        stage = new Stage(new ScreenViewport());

        this.game = game;
        this.gameEngineScreen = gameEngineScreen;
        this.manager = manager;

        // Close button
        Texture[] buttonStyles = new Texture[2];
        buttonStyles[0] = manager.get("core/assets/buttons/options-button.png", Texture.class);
        buttonStyles[1] = manager.get("core/assets/buttons/options-button-pressed.png", Texture.class);

        Sprite[] buttonStylesSprites = new Sprite[2];

        setButtonsSprites(buttonStyles, buttonStylesSprites, 5.f);

        gameMenuButton = new GameImageButton(GAME_WIDTH - 10, 100, this, buttonStylesSprites);
        gameMenuButton.setOptionsListener();

        setButtonsSprites(buttonStyles, buttonStylesSprites, 6.5f);

        // Play button
        buttonStyles[0] = manager.get("core/assets/buttons/ready-button.png", Texture.class);
        buttonStyles[1] = manager.get("core/assets/buttons/ready-button-active.png", Texture.class);
        playButtonGreenStyle = new Sprite(manager.get("core/assets/ui/ready-button-go.png", Texture.class));

        setButtonsSprites(buttonStyles, buttonStylesSprites, 6.5f);

        playButton = new GameImageButton(buttonStylesSprites);

        // Player Name textfield
        playersName = "Player";

        playersSetNameDialog = new Dialog("ENTER YOUR NAME", skin) {

            {
                playerNameTextField = new TextField(playersName, skin);
                this.text("");
                this.row();
                this.add(playerNameTextField);
                this.row();
                this.getCells().removeIndex(1);
                this.add(this.getButtonTable());
                this.button("ACCEPT");
            }

            @Override
            protected void result(final Object act) {
                playersName = playerNameTextField.getText();
                playerNameTextField = null;
                stage.addActor(layoutTable);
            }

        };

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_QUEEN.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_QUEEN.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);

        GameImageButton Queen = new GameImageButton(buttonStylesSprites, this);
        Queen.setOptionsListener("B_QUEEN");

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);
        GameImageButton Rook = new GameImageButton(buttonStylesSprites, this);
        Rook.setOptionsListener("B_ROOK");

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);
        GameImageButton Bishop = new GameImageButton(buttonStylesSprites, this);
        Bishop.setOptionsListener("B_BISHOP");

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);
        GameImageButton Knight = new GameImageButton(buttonStylesSprites, this);
        Knight.setOptionsListener("B_KNIGHT");


        pawnChangeDialog = new Dialog("CHOOSE REPLACEMENT FOR PAWN", skin) {

            {
                this.add(this.getButtonTable());
                this.button(Queen);
                this.button(Rook);
                this.button(Bishop);
                this.button(Knight);
                this.row();
                this.getCells().removeIndex(1);

            }

            @Override
            protected void result(final Object act) {
                stage.addActor(layoutTable);
                gameEngineScreen.resume();
            }
        };

        playersSetNameDialog.show(stage);

        // Table for play, repeat buttons
        layoutTable = new Table();
        layoutTable.right();
        layoutTable.setFillParent(true);
        layoutTable.add(playButton).expandX().padTop(400);

        stage.addActor(gameMenuButton);
    }

    // Constructor
    public Hud(AssetManager manager, Main game, GameEngine gameEngineScreen, Cursor kCursor, boolean isHost) {
        skin = new Skin();
        skin = manager.get("core/assets/buttons/skins/golden-spiral/skin/golden-ui-skin.json", Skin.class);
        cursor = kCursor;
        stage = new Stage(new ScreenViewport());

        this.game = game;
        this.gameEngineScreen = gameEngineScreen;
        this.manager = manager;

        // Close button
        Texture[] buttonStyles = new Texture[2];
        buttonStyles[0] = manager.get("core/assets/buttons/options-button.png", Texture.class);
        buttonStyles[1] = manager.get("core/assets/buttons/options-button-pressed.png", Texture.class);

        Sprite[] buttonStylesSprites = new Sprite[2];

        setButtonsSprites(buttonStyles, buttonStylesSprites, 5.f);

        gameMenuButton = new GameImageButton(GAME_WIDTH - 10, 100, this, buttonStylesSprites);
        gameMenuButton.setOptionsListener();

        setButtonsSprites(buttonStyles, buttonStylesSprites, 6.5f);

        // Play button
        buttonStyles[0] = manager.get("core/assets/buttons/ready-button.png", Texture.class);
        buttonStyles[1] = manager.get("core/assets/buttons/ready-button-active.png", Texture.class);
        playButtonGreenStyle = new Sprite(manager.get("core/assets/ui/ready-button-go.png", Texture.class));

        setButtonsSprites(buttonStyles, buttonStylesSprites, 6.5f);

        playButton = new GameImageButton(buttonStylesSprites);

        // Player Name textfield
        playersName = "Player";

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_QUEEN.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_QUEEN.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);

        GameImageButton Queen = new GameImageButton(buttonStylesSprites, this);
        Queen.setOptionsListener("B_QUEEN");

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);
        GameImageButton Rook = new GameImageButton(buttonStylesSprites, this);
        Rook.setOptionsListener("B_ROOK");

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);
        GameImageButton Bishop = new GameImageButton(buttonStylesSprites, this);
        Bishop.setOptionsListener("B_BISHOP");

        buttonStyles[0] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()], Texture.class);
        buttonStyles[1] = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()], Texture.class);

        setButtonsSprites(buttonStyles, buttonStylesSprites, 1.f);
        GameImageButton Knight = new GameImageButton(buttonStylesSprites, this);
        Knight.setOptionsListener("B_KNIGHT");


        pawnChangeDialog = new Dialog("CHOOSE REPLACEMENT FOR PAWN", skin) {

            {
                this.add(this.getButtonTable());
                this.button(Queen);
                this.button(Rook);
                this.button(Bishop);
                this.button(Knight);
                this.row();
                this.getCells().removeIndex(1);

            }

            @Override
            protected void result(final Object act) {
                stage.addActor(layoutTable);
                gameEngineScreen.resume();
            }
        };

        // Table for play, repeat buttons
        layoutTable = new Table();
        layoutTable.right();
        layoutTable.setFillParent(true);
        layoutTable.add(playButton).expandX().padTop(400);

        stage.addActor(layoutTable);
        stage.addActor(gameMenuButton);
    }

    // Methods

    /**
     * Metoda ustawiająca sprite'y przycisków.
     *
     * @param textures Tablica tekstur.
     * @param sprites  Tablica sprite'ów.
     * @param factor   Współczynnik rozmiaru.
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
     *
     * @return Przycisk do przejścia do rozgrywki.
     */
    public GameImageButton getPlayButton() {
        return playButton;
    }

    /**
     * Metoda typu get, zwraca String.
     *
     * @return Nazwa gracza.
     */
    public String getPlayersName() {
        return playersName;
    }

    /**
     * Metoda typu get, zwraca okno dialogowe.
     *
     * @return Okno dialogowe.
     */
    public Dialog getPlayersSetNameDialog() {
        return playersSetNameDialog;
    }

    /**
     * Metoda typu get, zwraca Stage.
     *
     * @return Scena elementów hud.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Metoda typu get, zwraca Skin.
     *
     * @return Skórka elementów gry.
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Metoda typu get, zwraca boolean.
     *
     * @return Czy włączone są opcje gry.
     */
    public boolean isPaused() {
        return gameMenuButton.getGameMenuState();
    }
}
