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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ourshipsgame.chess_pieces.*;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.handlers.Score;
import com.ourshipsgame.inteligentSystems.ComputerPlayerAi;

import static com.ourshipsgame.game.GameBoard.BoardLocations.*;

/**
 * Klasa abstrakcyjna zawierająca metody oraz obiekty i zmienne niezbędne do
 * funkcjonowania aplikacji
 */
public abstract class GameEngine extends ScreenAdapter implements Constant {

    // Important vars
    /*
    * Map of the background
    * */
    protected GameObject gameBackground;

    protected Player whitePlayer = new Player(Player.PlayerColor.WHITE);
    protected Player blackPlayer = new Player(Player.PlayerColor.BLACK);
    /*
    * Board of the game
    * */
    protected GameBoard gameBoard = new GameBoard();
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
     * Kursor
     */
    protected Cursor cursor;
    /**
     * Pixmapa kursorów
     */
    protected Pixmap crosshairPixmap;

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
    protected Chess[] whiteChesses = new Chess[16];
    /**
     * Tablica obiektów przechowujących czarne szachy
     */
    protected Chess[] blackChesses = new Chess[16];

    // more other vars
    /**
     * Zmienna do logiki click n drop statku w czasie ustawiania statków na planszy
     */
    protected Chess currentChessClicked ;
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
        // Crosshair
        manager.load("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        // Sound effects
        manager.load("core/assets/sounds/won.mp3", Sound.class);
        manager.load("core/assets/sounds/lose.mp3", Sound.class);

        //Board Textures
        manager.load("core/assets/backgroundtextures/ChessMenuBg.png", Texture.class);
        manager.load("core/assets/backgroundtextures/chessBoard.jpg", Texture.class);

        //Chess pieces Textures
        for (int i = 0; i < 12; i++)
            manager.load(ChessPiecesTexturesPaths[i],Texture.class);

        manager.load("core/assets/moves/attack.png",Texture.class);
        manager.load("core/assets/moves/move.png",Texture.class);
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
     * @param computerEnemy Określa czy przeciwnik to komputer
     * @param manager       AssetManager przechowuje zasoby załadowane
     * @return boolean Zwraca true po zakończeniu
     */
    // game methods below
    // Stage 1
    protected boolean preparation(boolean computerEnemy, AssetManager manager) {
        boolean done = false;

        crosshairPixmap = manager.get("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        endSounds[0] = manager.get("core/assets/sounds/won.mp3", Sound.class);
        endSounds[1] = manager.get("core/assets/sounds/lose.mp3", Sound.class);

        turnFont = manager.get("core/assets/fonts/nunito.light.ttf", BitmapFont.class);
        turnFontActive = manager.get("core/assets/fonts/nunito.light2.ttf", BitmapFont.class);

        gameBackground = new GameObject(
                manager.get("core/assets/backgroundtextures/ChessMenuBg.png",Texture.class),
                0,
                0,
                true,
                false,
                null
        );

        gameBoard.gameBoardObject = new GameObject(
                manager.get("core/assets/backgroundtextures/chessBoard.jpg",Texture.class),
                Constant.X_AXIS_BOARD_START,
                Constant.Y_AXIS_BOARD_START,
                true,
                false,
                null
        );

        whiteChesses[ChessPiecesInArray.King.ordinal()] = new King(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KING.ordinal()],Texture.class),
                E1,
                manager
        );

        whiteChesses[ChessPiecesInArray.Queen.ordinal()] = new Queen(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_QUEEN.ordinal()],Texture.class),
                E4,
                manager
        );

        whiteChesses[ChessPiecesInArray.FstBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()],Texture.class),
                C1,
                manager
        );

        whiteChesses[ChessPiecesInArray.SndBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()],Texture.class),
                F1,
                manager
        );

        whiteChesses[ChessPiecesInArray.FstKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()],Texture.class),
                B1,
                manager
        );

        whiteChesses[ChessPiecesInArray.SndKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()],Texture.class),
                G1,
                manager
        );

        whiteChesses[ChessPiecesInArray.FstRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()],Texture.class),
                D4,
                manager
        );

        whiteChesses[ChessPiecesInArray.SndRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()],Texture.class),
                H1,
                manager
        );

        whiteChesses[ChessPiecesInArray.Pawn1.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                A2,
                manager
        );
        whiteChesses[ChessPiecesInArray.Pawn2.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                B2,
                manager
        );
        whiteChesses[ChessPiecesInArray.Pawn3.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                C2,
                manager
        );
        whiteChesses[ChessPiecesInArray.Pawn4.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                D2,
                manager
        );
        whiteChesses[ChessPiecesInArray.Pawn5.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                E2,
                manager
        );
        whiteChesses[ChessPiecesInArray.Pawn6.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                F2,
                manager
        );
        whiteChesses[ChessPiecesInArray.Pawn7.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                G2,
                manager
        );
        whiteChesses[ChessPiecesInArray.Pawn8.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                H2,
                manager
        );

        ////////////////////////////////////////
        blackChesses[ChessPiecesInArray.King.ordinal()] = new King(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KING.ordinal()],Texture.class),
                E8,
                manager
        );

        blackChesses[ChessPiecesInArray.Queen.ordinal()] = new Queen(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_QUEEN.ordinal()],Texture.class),
                D8,
                manager
        );

        blackChesses[ChessPiecesInArray.FstBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()],Texture.class),
                C8,
                manager
        );

        blackChesses[ChessPiecesInArray.SndBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()],Texture.class),
                F6,
                manager
        );

        blackChesses[ChessPiecesInArray.FstKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()],Texture.class),
                B8,
                manager
        );

        blackChesses[ChessPiecesInArray.SndKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()],Texture.class),
                G8,
                manager
        );

        blackChesses[ChessPiecesInArray.FstRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()],Texture.class),
                B6,
                manager
        );

        blackChesses[ChessPiecesInArray.SndRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()],Texture.class),
                H8,
                manager
        );

        blackChesses[ChessPiecesInArray.Pawn1.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                A7,
                manager
        );
        blackChesses[ChessPiecesInArray.Pawn2.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                B7,
                manager
        );
        blackChesses[ChessPiecesInArray.Pawn3.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                C7,
                manager
        );
        blackChesses[ChessPiecesInArray.Pawn4.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                D7,
                manager
        );
        blackChesses[ChessPiecesInArray.Pawn5.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                E7,
                manager
        );
        blackChesses[ChessPiecesInArray.Pawn6.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                F7,
                manager
        );
        blackChesses[ChessPiecesInArray.Pawn7.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                G7,
                manager
        );
        blackChesses[ChessPiecesInArray.Pawn8.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                H7,
                manager
        );

        for (int i = 0; i < 16; i++) {
            whiteChesses[i].setPlayer(whitePlayer);
            blackChesses[i].setPlayer(blackPlayer);
        }

        PlayerOne.setPlayerName("TemplateName");

        if (computerEnemy) {
            PlayerOne.setPlayerName("Bot Clark");
        } else {
            PlayerTwo.setPlayerName("TemplateName");
        }


        int xHot = 0;
        int yHot = 0;
        cursor = Gdx.graphics.newCursor(crosshairPixmap, xHot, yHot);

        Gdx.graphics.setCursor(cursor);

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
