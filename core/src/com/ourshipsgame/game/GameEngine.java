package com.ourshipsgame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ourshipsgame.Main;
import com.ourshipsgame.chess_pieces.*;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.handlers.Score;
import com.ourshipsgame.hud.Hud;
import com.ourshipsgame.inteligentSystems.ComputerPlayerAi;
import com.ourshipsgame.utils.ChessMove;
import org.lwjgl.util.vector.Vector2f;

import java.text.NumberFormat;
import java.util.Arrays;

import static com.ourshipsgame.game.GameBoard.BoardLocations.*;

/**
 * Klasa abstrakcyjna zawierająca metody oraz obiekty i zmienne niezbędne do
 * funkcjonowania aplikacji
 */
public abstract class GameEngine extends ScreenAdapter implements Constant {

    // Important vars
    /**
     * AssetManager do ładowania zasobów gry
     */
    protected AssetManager manager;
    /**
     * Obiekt aplikacji
     */
    protected Main game;
    /**
     * Multiplekser do obsługi wejścia (klawisze/myszka etc)
     */
    protected InputMultiplexer inputMultiplexer;
    /**
     * Obiekt głowny interfejsu
     */
    protected Hud hud;
    /**
     * Obiekt do rysowania na ekranie
     */
    protected SpriteBatch sb;
    /**
     * Obiekt do rysowania kształtów na ekranie
     */
    protected ShapeRenderer sr;
    /**
     * Zmienna przechowująca progres ładowania zasobów
     */
    protected float progress;

    /**
     * Zmienna przechowująca historię gry
     */
    protected GameHistory gameHistory;
    /**
     * Zmienna określająca ,który to stopień gry do obliczeń logiki gry
     */
    public int gameStage = 1;
    /**
     * Tło do ekranu ładowania
     */
    protected Texture loadingTexture;
    /**
     * Zmienna określająca czy utworzono tekstury
     */
    protected boolean createdTextures = false;
    /**
     * Zmienna określająca czy należy stworzyć okno dialogowe z użytkownikiem
     */
    protected boolean createDialog = false;
    // other vars
    /**
     * Czcionka do ekranu ładowania
     */
    protected BitmapFont font;
    /*
     * Map of the background
     * */
    protected GameObject gameBackground;

    protected boolean loadGameFromFile;

    protected boolean pause;

    protected GameObject stage2MessageBackground;

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
    protected Player PlayerTurn = whitePlayer;

    protected Player MyPlayer;

    protected Player EnemyPlayer;

    protected GameObject[] TurnInfos = new GameObject[2];
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

    // Important Objects
    /**
     * Tablica obiektów przechowujących białe szachy
     */
    protected Chess[] whiteCheeses = new Chess[16];
    /**
     * Tablica obiektów przechowujących czarne szachy
     */
    protected Chess[] blackCheeses = new Chess[16];

    // more other vars
    /**
     * Zmienna do logiki click n drop statku w czasie ustawiania statków na planszy
     */
    protected Chess currentChessClicked;

    protected Chess pawnToChange;
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
     * Zmienna określająca czy Pierwszy Gracz przegrał
     */
    protected boolean PlayerOneLost = false;
    /**
     * Zmienna określająca czy Drugi Gracz przegrał
     */
    protected boolean PlayerTwoLost = false;
    /*
     * */
    protected boolean TieBetweenPlayers = false;

    protected boolean PlayerOneChecked = false;

    protected boolean PlayerTwoChecked = false;


    /**
     * Metoda do zmiany tury
     */
    protected void switchTurn() {
        if (PlayerTurn == MyPlayer)
            PlayerTurn = EnemyPlayer;
        else
            PlayerTurn = MyPlayer;

        calculateChessMoves();

        King whiteKing = (King) whiteCheeses[ChessPiecesInArray.King.ordinal()];
        whiteKing.checkKingCondition(blackCheeses, gameBoard);

        King blackKing = (King) blackCheeses[ChessPiecesInArray.King.ordinal()];
        blackKing.checkKingCondition(whiteCheeses, gameBoard);

        PlayerOneChecked = whiteKing.isChecked();
        PlayerTwoChecked = blackKing.isChecked();

        PlayerOneLost = whiteKing.isMated();
        PlayerTwoLost = blackKing.isMated();

        TieBetweenPlayers = whiteKing.isTie() | blackKing.isTie() | (PlayerOneLost && PlayerTwoLost);

        if (PlayerOneLost | PlayerTwoLost | TieBetweenPlayers)
            gameStage = 4;

    }

    protected void calculateChessMoves() {

        Arrays.stream(whiteCheeses).forEach(chess -> {
            if (!chess.isDestroyed()) {

                if (chess instanceof King king)
                    king.evaluateMoves(gameBoard, blackCheeses);
                else chess.evaluateMoves(gameBoard);
            }
        });

        Arrays.stream(blackCheeses).forEach(chess -> {
            if (!chess.isDestroyed()) {

                if (chess instanceof King king)
                    king.evaluateMoves(gameBoard, whiteCheeses);
                else chess.evaluateMoves(gameBoard);

            }
        });
    }


    public void saveGame() {
        gameHistory.historySave();
    }

    protected void loadGameFromFile() {
        gameHistory.historyLoad()
                .forEach(this::loadMove);

        PlayerTurn = gameHistory.getPlayerTurn();

        MyPlayer = gameHistory.getCurrentPlayer();

        EnemyPlayer = (MyPlayer == whitePlayer) ? blackPlayer : whitePlayer;

        if (EnemyPlayer == blackPlayer)
            enemyComputerPlayerAi = new ComputerPlayerAi(blackCheeses);
        else
            enemyComputerPlayerAi = new ComputerPlayerAi(whiteCheeses);

        enemyComputerPlayerAi.setPlayer(EnemyPlayer);
    }

    private void loadMove(ChessMove move) {
        move.getMoveLocation()
                .getChess()
                .moveChessWhileLoading(move.getMoveDestination());

        if (move.getMoveType() == ChessMove.typesOfMoves.CHANGEFIGURE) {
            pawnToChange = move.getMoveDestination().getChess();
            changePawn(move.getDesiredFigure().name());
            pawnToChange = null;
        }
    }

    public void changePawn(String Clazz) {

        if (pawnToChange == null)
            return;

        Texture texture;
        boolean isBlackPlayer = pawnToChange.getPlayer().getColor().equals(Player.PlayerColor.BLACK);
        Player pawnPlayer = pawnToChange.getPlayer();

        Chess[] cheeses = isBlackPlayer ? blackCheeses : whiteCheeses;

        int index = Arrays.stream(cheeses).toList().indexOf(pawnToChange);

        switch (Clazz) {

            case "B_BISHOP" -> {
                Clazz = isBlackPlayer ? Clazz : "W_BISHOP";
                texture = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.valueOf(Clazz).ordinal()], Texture.class);

                pawnToChange = new Bishop(
                        texture,
                        pawnToChange.getCurrentLocation(),
                        manager
                );
            }

            case "B_ROOK" -> {
                Clazz = isBlackPlayer ? Clazz : "W_ROOK";
                texture = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.valueOf(Clazz).ordinal()], Texture.class);

                pawnToChange = new Rook(
                        texture,
                        pawnToChange.getCurrentLocation(),
                        manager
                );
            }

            case "B_QUEEN" -> {
                Clazz = isBlackPlayer ? Clazz : "W_QUEEN";
                texture = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.valueOf(Clazz).ordinal()], Texture.class);

                pawnToChange = new Queen(
                        texture,
                        pawnToChange.getCurrentLocation(),
                        manager
                );
            }

            case "B_KNIGHT" -> {
                Clazz = isBlackPlayer ? Clazz : "W_KNIGHT";
                texture = manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.valueOf(Clazz).ordinal()], Texture.class);

                pawnToChange = new Knight(
                        texture,
                        pawnToChange.getCurrentLocation(),
                        manager
                );
            }

        }

        pawnToChange.setPlayer(pawnPlayer);
        cheeses[index] = pawnToChange;

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
        manager.load("core/assets/sounds/attack_sound.wav", Sound.class);
        manager.load("core/assets/sounds/move_sound.wav", Sound.class);

        //Board Textures
        manager.load("core/assets/backgroundtextures/ChessMenuBg.png", Texture.class);
        manager.load("core/assets/backgroundtextures/chessBoard.jpg", Texture.class);
        manager.load("core/assets/backgroundtextures/turn-info-texture.png", Texture.class);
        manager.load("core/assets/backgroundtextures/message_bg.png", Texture.class);

        //Chess pieces Textures
        for (int i = 0; i < 12; i++)
            manager.load(ChessPiecesTexturesPaths[i], Texture.class);

        manager.load("core/assets/moves/attack.png", Texture.class);
        manager.load("core/assets/moves/move.png", Texture.class);
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
        manager.load("core/assets/buttons/options-button.png", Texture.class);
        manager.load("core/assets/buttons/options-button-pressed.png", Texture.class);
        // Play button
        manager.load("core/assets/buttons/ready-button.png", Texture.class);
        manager.load("core/assets/buttons/ready-button-active.png", Texture.class);
        manager.load("core/assets/ui/ready-button-go.png", Texture.class);

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
     * @param manager AssetManager przechowuje zasoby załadowane
     * @return boolean Zwraca true po zakończeniu
     */
    // game methods below
    // Stage 1
    protected boolean preparation(AssetManager manager) {

        boolean done = false;

        crosshairPixmap = manager.get("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        endSounds[0] = manager.get("core/assets/sounds/won.mp3", Sound.class);
        endSounds[1] = manager.get("core/assets/sounds/lose.mp3", Sound.class);

        turnFont = manager.get("core/assets/fonts/nunito.light.ttf", BitmapFont.class);
        turnFontActive = manager.get("core/assets/fonts/nunito.light2.ttf", BitmapFont.class);

        stage2MessageBackground = new GameObject(
                manager.get("core/assets/backgroundtextures/message_bg.png", Texture.class),
                Constant.GAME_WIDTH_F / 2 - 400,
                Constant.GAME_HEIGHT_F / 2 - 165,
                true,
                false,
                null
        );

        gameBackground = new GameObject(
                manager.get("core/assets/backgroundtextures/ChessMenuBg.png", Texture.class),
                0,
                0,
                true,
                false,
                null
        );

        gameBoard.gameBoardObject = new GameObject(
                manager.get("core/assets/backgroundtextures/chessBoard.jpg", Texture.class),
                Constant.X_AXIS_BOARD_START,
                Constant.Y_AXIS_BOARD_START,
                true,
                false,
                null
        );

        TurnInfos[0] = new GameObject(
                manager.get("core/assets/backgroundtextures/turn-info-texture.png", Texture.class),
                new Vector2f(400, 140),
                Constant.X_AXIS_BOARD_START - 400,
                gameHeight_f - 140,
                true,
                false,
                null

        );

        TurnInfos[1] = new GameObject(
                manager.get("core/assets/backgroundtextures/turn-info-texture.png", Texture.class),
                new Vector2f(400, 140),
                Constant.X_AXIS_BOARD_START + (9 * 122),
                gameHeight_f - 140,
                true,
                false,
                null

        );


        whiteCheeses[ChessPiecesInArray.King.ordinal()] = new King(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KING.ordinal()], Texture.class),
                E1,
                manager
        );

        whiteCheeses[ChessPiecesInArray.Queen.ordinal()] = new Queen(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_QUEEN.ordinal()], Texture.class),
                E4,
                manager
        );

        whiteCheeses[ChessPiecesInArray.FstBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()], Texture.class),
                C1,
                manager
        );

        whiteCheeses[ChessPiecesInArray.SndBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()], Texture.class),
                F1,
                manager
        );

        whiteCheeses[ChessPiecesInArray.FstKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()], Texture.class),
                B1,
                manager
        );

        whiteCheeses[ChessPiecesInArray.SndKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()], Texture.class),
                G1,
                manager
        );

        whiteCheeses[ChessPiecesInArray.FstRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()], Texture.class),
                D4,
                manager
        );

        whiteCheeses[ChessPiecesInArray.SndRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()], Texture.class),
                H1,
                manager
        );

        whiteCheeses[ChessPiecesInArray.Pawn1.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                A7,
                manager
        );
        whiteCheeses[ChessPiecesInArray.Pawn2.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                B2,
                manager
        );
        whiteCheeses[ChessPiecesInArray.Pawn3.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                C2,
                manager
        );
        whiteCheeses[ChessPiecesInArray.Pawn4.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                D2,
                manager
        );
        whiteCheeses[ChessPiecesInArray.Pawn5.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                E2,
                manager
        );
        whiteCheeses[ChessPiecesInArray.Pawn6.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                F2,
                manager
        );
        whiteCheeses[ChessPiecesInArray.Pawn7.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                G2,
                manager
        );
        whiteCheeses[ChessPiecesInArray.Pawn8.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()], Texture.class),
                H2,
                manager
        );

        ////////////////////////////////////////
        blackCheeses[ChessPiecesInArray.King.ordinal()] = new King(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KING.ordinal()], Texture.class),
                E8,
                manager
        );

        blackCheeses[ChessPiecesInArray.Queen.ordinal()] = new Queen(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_QUEEN.ordinal()], Texture.class),
                D8,
                manager
        );

        blackCheeses[ChessPiecesInArray.FstBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()], Texture.class),
                C8,
                manager
        );

        blackCheeses[ChessPiecesInArray.SndBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()], Texture.class),
                F6,
                manager
        );

        blackCheeses[ChessPiecesInArray.FstKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()], Texture.class),
                B8,
                manager
        );

        blackCheeses[ChessPiecesInArray.SndKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()], Texture.class),
                G8,
                manager
        );

        blackCheeses[ChessPiecesInArray.FstRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()], Texture.class),
                B6,
                manager
        );

        blackCheeses[ChessPiecesInArray.SndRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()], Texture.class),
                H8,
                manager
        );

        blackCheeses[ChessPiecesInArray.Pawn1.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                A5,
                manager
        );
        blackCheeses[ChessPiecesInArray.Pawn2.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                B7,
                manager
        );
        blackCheeses[ChessPiecesInArray.Pawn3.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                C7,
                manager
        );
        blackCheeses[ChessPiecesInArray.Pawn4.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                D7,
                manager
        );
        blackCheeses[ChessPiecesInArray.Pawn5.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                E7,
                manager
        );
        blackCheeses[ChessPiecesInArray.Pawn6.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                F7,
                manager
        );
        blackCheeses[ChessPiecesInArray.Pawn7.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                G7,
                manager
        );
        blackCheeses[ChessPiecesInArray.Pawn8.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()], Texture.class),
                H7,
                manager
        );

        for (int i = 0; i < 16; i++) {
            whiteCheeses[i].setPlayer(whitePlayer);
            blackCheeses[i].setPlayer(blackPlayer);
        }

        int xHot = 0;
        int yHot = 0;
        cursor = Gdx.graphics.newCursor(crosshairPixmap, xHot, yHot);

        Gdx.graphics.setCursor(cursor);

        return true;
    }

    /**
     * Metoda do renderowania ekranu ładowania
     */
    protected void drawLoadingScreen() {
        progress = manager.getProgress();
        sb.begin();
        sb.draw(loadingTexture, 0, 0);
        String load = "Loading " + NumberFormat.getPercentInstance().format(progress);
        font.draw(sb, load, (gameWidth_f / 2f) - 175, (gameHeight_f / 2f) + 43);
        sb.end();
    }

    /**
     * Metoda do renderowania wiadomości po bitwie
     */
    protected void drawExitScreen() {
        String msg;
        boolean imPlayerOne = MyPlayer == whitePlayer;

        if (PlayerOneLost)
            if (imPlayerOne) {
                msg = "You 've Lost!! Better luck next time!";
                font.draw(sb, msg, (gameWidth_f / 2) - 350, gameHeight_f / 2 + 400);
            } else {
                msg = "You 've Won!! Keep it up!!";
                font.draw(sb, msg, (gameWidth_f / 2) - 250, gameHeight_f / 2 + 400);
            }
        else if (PlayerTwoLost)
            if (imPlayerOne) {
                msg = "You 've Won!! Keep it up!!";
                font.draw(sb, msg, (gameWidth_f / 2) - 250, gameHeight_f / 2 + 400);
            } else {
                msg = "You 've Lost!! Better luck next time!";
                font.draw(sb, msg, (gameWidth_f / 2) - 350, gameHeight_f / 2 + 400);
            }
    }

    // Create methods

    /**
     * Metoda do utworzenia czcionek
     */
    protected void createFonts() {
        font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("core/assets/fonts/Raleway-ExtraLightItalic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 43;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.WHITE;
        parameter.color = Color.RED;
        font = generator.generateFont(parameter);
        parameter.size = 16;
        parameter.borderWidth = 0;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.GOLD;
        hudFont = generator.generateFont(parameter);
        generator.dispose();
    }

    /**
     * Metoda do ładowania wszystkich zasobów gry
     */
    protected void loadAssets() {
        loadGameEngine(manager);
        loadHudAssets(manager);
    }


    /**
     * Metoda do renderowania mapy
     */
    protected void drawMap() {
        gameBackground.getSprite().draw(sb);
        gameBoard.gameBoardObject.getSprite().draw(sb);
    }

    /**
     * Metoda do renderowania szachów
     */
    protected void drawChessPieces() {
        for (int i = 0; i < 16; i++) {
            whiteCheeses[i].getGameObject().getSprite().draw(sb);

            blackCheeses[i].getGameObject().getSprite().draw(sb);
        }
    }

    /**
     * Metoda do rysowania tekstu pomocy w czasie przed bitwą
     *
     * @param font  Czcionka do tekstu
     * @param batch SpriteBatch do rysowania na ekranie
     */
    protected void drawStage2Text(BitmapFont font, SpriteBatch batch) {
        int fontSize = 20;
        stage2MessageBackground.drawSprite(batch);
        Vector2f stage2MessageBackgroundPosition = stage2MessageBackground.getPosition();

        String text = "You are " + MyPlayer.getColor().name() + " Player";
        int len = text.length();
        font.draw(batch,
                text,
                (stage2MessageBackgroundPosition.getX() + (stage2MessageBackground.getWidth() / 2) - (fontSize * (len / 2.f))),
                stage2MessageBackgroundPosition.getY() + 165 + 25);


        text = "Click Ready to start the game!";
        len = text.length();
        font.draw(batch,
                text,
                (stage2MessageBackgroundPosition.getX() + (stage2MessageBackground.getWidth() / 2) - (fontSize * (len / 2.f))),
                stage2MessageBackgroundPosition.getY() + 165 - 35);
    }

    protected void drawCurrentClickedChessAvailableMoves() {
        if (currentChessClicked != null)
            currentChessClicked.drawAvailableMovesAndAttacks(sb);
    }

    protected void createAndDisplay(float deltaTime, InputProcessor processor) {
        if (manager.update()) {
            // When loading screen disappers
            if (!createdTextures) {
                loadingTexture.dispose();
                createGraphics();
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(processor);
                inputMultiplexer.addProcessor(hud.getStage());
                Gdx.input.setInputProcessor(inputMultiplexer);
                // tmp
            }
            if (hud.isPaused())
                Gdx.input.setInputProcessor(hud.getStage());
            else
                Gdx.input.setInputProcessor(inputMultiplexer);


            // update
            update(deltaTime);
            // render things below
            sb.begin();
            sr.setAutoShapeType(true);
            sr.begin();
            // Do not place any drawings up!!

            // Texts
            switch (gameStage) {
                case 2 -> {
                    drawMap();
                    drawChessPieces();
                    drawStage2Text(font, sb);
                }
                case 3 -> {
                    drawMap();
                    drawCurrentClickedChessAvailableMoves();
                    drawChessPieces();
                    drawTurnInfo(sb);
                    drawKingCondition(sb);
                }
                case 4 -> {
                    drawMap();
                    drawChessPieces();
                    drawExitScreen();
                }
            }

            sb.end();
            sr.end();
            hud.update();
        } else {
            // While loading the game assets
            drawLoadingScreen();
        }
    }

    protected void drawKingCondition(SpriteBatch batch) {

        boolean imPlayerOne = MyPlayer == whitePlayer;

        if (PlayerOneChecked) {

            if (imPlayerOne)
                turnFontActive.draw(batch,
                        "Your King is Checked!",
                        TurnInfos[0].getPosition().getX() + 64,
                        TurnInfos[0].getPosition().getY() - 96);

            else turnFontActive.draw(batch,
                    "Enemy King is Checked!",
                    TurnInfos[1].getPosition().getX() + 64,
                    TurnInfos[1].getPosition().getY() - 96);
        }

        if (PlayerTwoChecked) {

            if (imPlayerOne)
                turnFontActive.draw(batch,
                        "Enemy King is Checked!",
                        TurnInfos[1].getPosition().getX() + 64,
                        TurnInfos[1].getPosition().getY() - 96);

            else turnFontActive.draw(batch,
                    "Your King is Checked!",
                    TurnInfos[0].getPosition().getX() + 64,
                    TurnInfos[0].getPosition().getY() - 96);

        }


    }

    private String formattedTimeLeft(Float time) {
        int minutes = (int) (time / 60);
        int seconds = (int) (time - (minutes * 60));

        return String.format("%s.%s", minutes, seconds);
    }

    protected void drawTurnInfo(SpriteBatch batch) {
        int fontSize = 8;
        Player player;
        for (int i = 0; i < 2; i++) {
            TurnInfos[i].drawSprite(batch);

            if (i == 0)
                player = MyPlayer;
            else player = EnemyPlayer;

            turnFontActive.draw(batch,
                    player.getPlayerName(),
                    TurnInfos[i].getPosition().getX() + 64,
                    TurnInfos[i].getPosition().getY() + 96);
            turnFont.draw(batch,
                    "Score:" + player.getScore().toString(),
                    TurnInfos[i].getPosition().getX() + TurnInfos[i].getWidth() - (("Score:" + player.getScore().toString()).length() * fontSize + fontSize) - 96,
                    TurnInfos[i].getPosition().getY() + 96);
            turnFont.draw(batch,
                    formattedTimeLeft(player.getTimeLeft()),
                    TurnInfos[i].getPosition().getX() + 64,
                    TurnInfos[i].getPosition().getY() + 48);


        }

        if (PlayerTurn == MyPlayer) {
            turnFontActive.draw(batch,
                    "Your Turn!",
                    TurnInfos[0].getPosition().getX() + TurnInfos[0].getSprite().getWidth() / 2 - ("Your Turn!".length() * fontSize),
                    TurnInfos[0].getPosition().getY() - 32);
            turnFont.draw(batch,
                    "Enemy Turn!",
                    TurnInfos[1].getPosition().getX() + TurnInfos[1].getSprite().getWidth() / 2 - ("Enemy Turn!".length() * fontSize),
                    TurnInfos[1].getPosition().getY() - 32);
        } else {
            turnFont.draw(batch,
                    "Your Turn!",
                    TurnInfos[0].getPosition().getX() + TurnInfos[0].getSprite().getWidth() / 2 - ("Your Turn!".length() * fontSize),
                    TurnInfos[0].getPosition().getY() - 32);
            turnFontActive.draw(batch,
                    "Enemy Turn!",
                    TurnInfos[1].getPosition().getX() + TurnInfos[1].getSprite().getWidth() / 2 - ("Enemy Turn!".length() * fontSize),
                    TurnInfos[1].getPosition().getY() - 32);
        }

    }

    protected abstract void update(float deltaTime);

    protected abstract void createGraphics();


    /**
     * Metoda do zwalniania zasobów wykorzystywanych przez klasę
     */
    @Override
    public void dispose() {
        //clearing chess objects positons
        for (int i = 0; i < GameBoard.BoardLocations.values().length; i++)
            GameBoard.BoardLocations.values()[i].setChess(null, true);
        inputMultiplexer.clear();
        sb.dispose();
        sr.dispose();
        manager.dispose();
        loadingTexture.dispose();
        font.dispose();
        hud.gameSettings.dispose();
        hud.dispose();
        hudFont.dispose();
        super.dispose();
    }

    @Override
    public void pause() {
        super.pause();
        pause = true;
    }

    @Override
    public void resume() {
        super.resume();
        pause = false;
    }
}
