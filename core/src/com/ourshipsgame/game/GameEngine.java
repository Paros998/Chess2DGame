package com.ourshipsgame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ourshipsgame.chess_pieces.Chess;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.handlers.Score;
import com.ourshipsgame.inteligentSystems.ComputerPlayerAi;

import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

/**
 * Klasa abstrakcyjna zawierająca metody oraz obiekty i zmienne niezbędne do
 * funkcjonowania aplikacji
 */
public abstract class GameEngine extends ScreenAdapter implements Constant {

    // Important vars
    /**
     * Obiekt przechowujący informacje o wynikach gracza
     */
    protected Score PlayerOne = new Score(1);
    /**
     * Obiekt przechowujący informacje o wynikach komputera
     */
    protected Score PlayerTwo = new Score(2);
    /**
     * Obiekt obliczający decyzje komputera
     */
    protected ComputerPlayerAi enemyComputerPlayerAi;
    /**
     * Zmienna okreslająca czyja tura jest aktualnie
     */
    protected int PlayerTurn;
    /**
     * Tablica kursorów
     */
    protected Cursor[] crosshairs = new Cursor[3];
    /**
     * Kursor
     */
    protected Cursor cursor;
    /**
     * Pixmapa kursorów
     */
    protected Pixmap[] crosshairPixmaps = new Pixmap[3];

    /**
     * Zmienna przechowująca wysokość okna w pikselach
     */
    protected int gameHeight = GAME_HEIGHT;
    /**
     * Zmienna przechowująca szerokość okna w pikselach
     */
    protected int gameWidth = GAME_WIDTH;
    /**
     * Zmienna przechowująca wysokość okna w pikselach
     */
    protected float gameHeight_f = GAME_HEIGHT_F;
    /**
     * Zmienna przechowująca szerokość okna w pikselach
     */
    protected float gameWidth_f = GAME_WIDTH_F;
    /**
     * Czcionka do interfejsu
     */
    protected BitmapFont hudFont;
    /**
     * Czcionka do tekstu tury nieaktywnej
     */
    protected BitmapFont turnFont;
    /**
     * Czcionka do tekstu tury aktywnej
     */
    protected BitmapFont turnFontActive;
    // Sounds and music
    /**
     * Tablica dźwięków końcowych
     */
    protected Sound[] endSounds = new Sound[2];
    /**
     * Dźwięk ruchu pionków
     */
    protected Sound moveSound;

    // Important Objects
    /**
     * Tablica obiektów przechowujących białe szachy
     */
    protected Chess[] WhiteChesses = new Chess[16];
    /**
     * Tablica obiektów przechowujących czarne szachy
     */
    protected Chess[] BlackChesses = new Chess[16];

    // more other vars
    /**
     * Zmienna do logiki click n drop statku w czasie ustawiania statków na planszy
     */
    protected int activeSpriteDrag = 99;
    /**
     * Zmienna przechowująca pozycje x sprite'a
     */
    protected float xSprite;
    /**
     * Zmienna przechowująca pozycje y sprite'a
     */
    protected float ySprite;
    /**
     * Zmienna przechowująca róznicę w pozycji w osi X
     */
    protected float xDiff;
    /**
     * Zmienna przechowująca róznicę w pozycji w osi Y
     */
    protected float yDiff;
    /**
     * Zmienna określająca czy Gracz przegrał
     */
    protected boolean PlayerOneLost = false;
    /**
     * Zmienna określająca czy Komputer przegrał
     */
    protected boolean PlayerTwoLost = false;

    /**
     * Metoda do zmiany tury
     */
    protected void switchTurn() {
        if (PlayerTurn == 1)
            PlayerTurn = 2;
        else
            PlayerTurn = 1;
    }

    /**
     * Metoda do ładowania assetów gry do AssetManagera
     * 
     * @param manager AssetManager
     */
    // loading method
    protected void loadGameEngine(AssetManager manager) {
        // Crosshairs
        manager.load("core/assets/cursors/crosshairRed.png", Pixmap.class);
        manager.load("core/assets/cursors/crosshairGreen.png", Pixmap.class);
        manager.load("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        // Sound effects
        manager.load("core/assets/sounds/won.mp3", Sound.class);
        manager.load("core/assets/sounds/lose.mp3", Sound.class);
    }

    /**
     * Metoda do ładowania assetów interfejsu do AssetManagera
     * 
     * @param manager AssetManager
     */
    protected void loadHudAssets(AssetManager manager) {
        // Skin
        manager.load("core/assets/buttons/skins/golden-spiral/skin/golden-ui-skin.json", Skin.class);
        // Options button
        manager.load("core/assets/ui/ui.hud/ui/global/modern/gear.png", Texture.class);
        manager.load("core/assets/ui/ui.hud/ui/global/modern/gear-press.png", Texture.class);
        // Play button
        manager.load("core/assets/ui/ready-button.png", Texture.class);
        manager.load("core/assets/ui/ready-button-pressed.png", Texture.class);
        manager.load("core/assets/ui/ready-button-go.png", Texture.class);
        // Repeat button
        manager.load("core/assets/ui/reverse-button-pressed.png", Texture.class);
        manager.load("core/assets/ui/reverse-button.png", Texture.class);
        // TTF Font
        manager.setLoader(FreeTypeFontGenerator.class,
                new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(new InternalFileHandleResolver()));
        FreetypeFontLoader.FreeTypeFontLoaderParameter param = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        param.fontFileName = "core/assets/fonts/nunito.light.ttf";
        param.fontParameters.size = 28;
        param.fontParameters.color = Color.GRAY;
        param.fontParameters.borderColor = Color.DARK_GRAY;
        param.fontParameters.borderWidth = 2;
        manager.load("core/assets/fonts/nunito.light.ttf", BitmapFont.class, param);
        FreetypeFontLoader.FreeTypeFontLoaderParameter param2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        param2.fontFileName = "core/assets/fonts/nunito.light2.ttf";
        param2.fontParameters.size = 28;
        param2.fontParameters.color = Color.GOLD;
        param2.fontParameters.borderColor = Color.DARK_GRAY;
        param2.fontParameters.borderWidth = 2;
        manager.load("core/assets/fonts/nunito.light2.ttf", BitmapFont.class, param2);
    }

    /**
     * Metoda do utworzenia faktycznych obiektów i zmiennych do gry
     * 
     * @param computerEnemy Określa czy przeciwniki to komputer
     * @param manager       AssetManager przechowuje zasoby załadowane
     * @return boolean Zwraca true po skończeniu
     */
    // game methods below
    // Stage 1
    protected boolean preparation(boolean computerEnemy, AssetManager manager) {
        boolean done = false;

        crosshairPixmaps[0] = manager.get("core/assets/cursors/crosshairRed.png", Pixmap.class);
        crosshairPixmaps[1] = manager.get("core/assets/cursors/crosshairGreen.png", Pixmap.class);
        crosshairPixmaps[2] = manager.get("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        endSounds[0] = manager.get("core/assets/sounds/won.mp3", Sound.class);
        endSounds[1] = manager.get("core/assets/sounds/lose.mp3", Sound.class);

        turnFont = manager.get("core/assets/fonts/nunito.light.ttf", BitmapFont.class);
        turnFontActive = manager.get("core/assets/fonts/nunito.light2.ttf", BitmapFont.class);

        PlayerOne.setPlayerName("TemplateName");
        PlayerTwo.setPlayerName("TemplateName");
        PlayerTurn = 1;

        int xHot = crosshairPixmaps[0].getWidth() / 2;
        int yHot = crosshairPixmaps[0].getHeight() / 2;
        crosshairs[0] = Gdx.graphics.newCursor(crosshairPixmaps[0], xHot, yHot);
        xHot = crosshairPixmaps[1].getWidth() / 2;
        yHot = crosshairPixmaps[1].getHeight() / 2;
        crosshairs[1] = Gdx.graphics.newCursor(crosshairPixmaps[1], xHot, yHot);
        xHot = 0;
        yHot = 0;
        crosshairs[2] = Gdx.graphics.newCursor(crosshairPixmaps[2], xHot, yHot);

        Gdx.graphics.setCursor(crosshairs[2]);

        return true;
    }

    /**
     * Metoda do określania na który szach kliknięto
     * myszki
     * 
     * @param screenX Pozycja x na ekranie
     * @param screenY Pozycja y na ekranie
     */
    // Stage 3 methods to click on a chess
    protected void touchDownSprite(int screenX, int screenY) {
//        for (int i = 0; i < sum; i++) {
//            if (FirstBoardShipsSprites[i].spriteContains(new Vector2(screenX, gameHeight_f - screenY))) {
//                activeSpriteDrag = i;
//            }
//        }
    }

    /**
     * Metoda do poruszania spriteów na planszy click n drop
     * 
     * @param screenX Nowa pozycja X na ekranie
     * @param screenY Nowa pozycja Y na ekranie
     */
    protected void moveChess(int screenX, int screenY, Chess chess) {

    }

    /**
     * Metoda do rysowania tekstu pomocy w czasie przed bitwą
     * 
     * @param font  Czcionka do tekstu
     * @param batch SpriteBatch do rysowania na ekranie
     */
    protected void drawStage2Text(BitmapFont font, SpriteBatch batch) {
        String text = "Lets draw who starts first!";
        int len = text.length();
        font.draw(batch, text, (gameWidth_f - 200 - (43 * (len / 2))), gameHeight_f / 2 + 200);
        text = "Confirm it by clicking READY button !";
        len = text.length();
        font.draw(batch, text, (gameWidth_f - 180 - (43 * (len / 2))), gameHeight_f / 2 + 100);
    }


    /**
     * Metoda do zwalniania zasobów wykorzystywanych przez klasę
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
