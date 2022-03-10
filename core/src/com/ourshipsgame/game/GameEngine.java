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
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.handlers.Score;
import com.ourshipsgame.inteligentSystems.ComputerPlayerAi;

import org.lwjgl.util.vector.Vector2f;

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
     * Tablica obiektów przechowujących wszystko o statkach na pierwszej planszy
     */
    protected GameObject FirstBoardShipsSprites[] = new GameObject[sum];
    /**
     * Tablica obiektów przechowujących wszystko o statkach na drugiej planszy
     */
    protected GameObject SecondBoardShipsSprites[] = new GameObject[sum];
    /**
     * Tablica obiektów przechowujących efekty wystrzału statków z pierwszej planszy
     */
    protected ShootParticleEffect shootEffect[] = new ShootParticleEffect[sum];
    // more other vars
    /**
     * Zmienna do logiki drag n drop statku w czasie ustawiania statków na planszy
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
     * Zmienna określająca czy można obracać wieżyczki
     */
    protected boolean rotateEnabled = false;
    /**
     * Zmienna określająca czy można strzelać
     */
    protected boolean shootingEnabled = false;
    /**
     * Zmienna określająca czy strzał się zakończył
     */
    protected boolean shootingDone = true;
    /**
     * Zmienna określająca czy trafiono po strzale
     */
    protected boolean hitted = false;
    /**
     * Zmienna określająca czy nietrafiono po strzale
     */
    protected boolean missed = false;
    /**
     * Zmienna określająca czy zniszczono okręt po strzale
     */
    protected boolean destroyed = false;
    /**
     * Zmienna określająca czy można grać dźwięk zniszczenia okrętu
     */
    protected boolean destroymentSound = false;
    /**
     * Zmienna określająca czy Gracz przegrał
     */
    protected boolean PlayerOneLost = false;
    /**
     * Zmienna określająca czy Komputer przegrał
     */
    protected boolean PlayerTwoLost = false;
    /**
     * Zmienna określająca pozycję trafienia
     */
    protected Vector2f hitPos = new Vector2f();
    /**
     * Zmienna określająca pozycję nietrafienia
     */
    protected Vector2f missPos = new Vector2f();
    /**
     * Zmienna określająca pozycję zniszczenia
     */
    protected Vector2f destroymentPos = new Vector2f();
    /**
     * Zmienna służąca do aktualizacji logiki związanej oddaniem strzału
     */
    protected float shootTime;

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

        manager.load("core/assets/oneship/three/threeshipModel.png", Texture.class);
        manager.load("core/assets/oneship/three/threeshipModelwaves.png", Texture.class);
        manager.load("core/assets/oneship/three/threeshipModelDestroyed.png", Texture.class);
        manager.load("core/assets/oneship/two/twoshipModel.png", Texture.class);
        manager.load("core/assets/oneship/two/twoshipModelwaves.png", Texture.class);
        manager.load("core/assets/oneship/two/twoshipModelDestroyed.png", Texture.class);
        manager.load("core/assets/oneship/one/oneshipModel.png", Texture.class);
        manager.load("core/assets/oneship/one/oneshipModelwaves.png", Texture.class);
        manager.load("core/assets/oneship/one/oneshipModelDestroyed.png", Texture.class);
        // Turret rotation sound
        manager.load("core/assets/sounds/TurretRotation.mp3", Sound.class);
        // Shoot effect
        manager.load("core/assets/animations/boom3.png", Texture.class);
        // Shoot sounds
        manager.load("core/assets/sounds/shoot/DeathFlash.mp3", Sound.class);
        manager.load("core/assets/sounds/shoot/explode.wav", Sound.class);
        manager.load("core/assets/sounds/shoot/explodemini.wav", Sound.class);
        manager.load("core/assets/sounds/shoot/ExplosionMetal.wav", Sound.class);
        manager.load("core/assets/sounds/shoot/ExplosionMetalGverb.wav", Sound.class);
        manager.load("core/assets/sounds/shoot/GunShot.wav", Sound.class);
        manager.load("core/assets/sounds/shoot/GunShotGverb.wav", Sound.class);
        manager.load("core/assets/sounds/shoot/BangLong.ogg", Sound.class);
        manager.load("core/assets/sounds/shoot/BangMid.ogg", Sound.class);
        manager.load("core/assets/sounds/shoot/BangSmall.ogg", Sound.class);
        manager.load("core/assets/sounds/shoot/rock_breaking.mp3", Sound.class);
        manager.load("core/assets/sounds/shoot/synthetic_explosion_1.mp3", Sound.class);
        // Animations sounds
        manager.load("core/assets/sounds/explosion/Chunky Explosion.mp3", Sound.class);
        manager.load("core/assets/sounds/miss/WaterSurfaceExplosion08.wav", Sound.class);
        // Animations textures
        manager.load("core/assets/animations/hitExplosion.png", Texture.class);
        manager.load("core/assets/animations/splash2.png", Texture.class);
        manager.load("core/assets/animations/shipDestroyedExplosion.png", Texture.class);
        // Crosshairs
        manager.load("core/assets/cursors/crosshairRed.png", Pixmap.class);
        manager.load("core/assets/cursors/crosshairGreen.png", Pixmap.class);
        manager.load("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);
        // Icons
        manager.load("core/assets/oneship/three/threeshipModelIcon.png", Texture.class);
        manager.load("core/assets/oneship/two/twoshipModelIcon.png", Texture.class);
        manager.load("core/assets/oneship/one/oneshipModelIcon.png", Texture.class);
        // Marks
        manager.load("core/assets/backgroundtextures/blackcross.png", Texture.class);
        manager.load("core/assets/backgroundtextures/redcross.png", Texture.class);
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


        rotateSound = manager.get("core/assets/sounds/TurretRotation.mp3", Sound.class);
        crosshairPixmaps[0] = manager.get("core/assets/cursors/crosshairRed.png", Pixmap.class);
        crosshairPixmaps[1] = manager.get("core/assets/cursors/crosshairGreen.png", Pixmap.class);
        crosshairPixmaps[2] = manager.get("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        endSounds[0] = manager.get("core/assets/sounds/won.mp3", Sound.class);
        endSounds[1] = manager.get("core/assets/sounds/lose.mp3", Sound.class);
        turnFont = manager.get("core/assets/fonts/nunito.light.ttf", BitmapFont.class);
        turnFontActive = manager.get("core/assets/fonts/nunito.light2.ttf", BitmapFont.class);
        PlayerOne.setPlayerName("TemplateName");
        PlayerTwo.setPlayerName("Computer");
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
    // Stage 3 methods to place ships on board
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
    protected void moveSprite(int screenX, int screenY) {
        if (activeSpriteDrag <= sum - 1 && activeSpriteDrag >= 0) {
            GameObject actualShip = FirstBoardShipsSprites[activeSpriteDrag];
            xSprite = actualShip.width / 2;
            ySprite = actualShip.height / 2;
            float box_size = 64f;

            float xChange = screenX - actualShip.x - xSprite;
            float yChange = gameHeight_f - screenY - actualShip.y - ySprite;

            if (xChange >= box_size && yChange >= box_size)
                actualShip.translate(new Vector2(box_size, box_size));

            else if (xChange <= -box_size && yChange <= -box_size)
                actualShip.translate(new Vector2(-box_size, -box_size));

            else if (xChange >= box_size && yChange <= -box_size)
                actualShip.translate(new Vector2(box_size, -box_size));

            else if (xChange <= -box_size && yChange >= box_size)
                actualShip.translate(new Vector2(-box_size, box_size));

            else if (xChange >= box_size)
                actualShip.translateX(box_size);

            else if (xChange <= -box_size)
                actualShip.translateX(-box_size);

            else if (yChange >= box_size)
                actualShip.translateY(box_size);

            else if (yChange <= -box_size)
                actualShip.translateY(-box_size);

        }
    }

    /**
     * Metoda do sprawdzania czy statek znajduje się w dopuszczalnej pozycji na
     * planszy
     * 
     * @param actualShip  Aktualnie sprawdzany statek
     * @param boardNumber Numer planszy
     * @return boolean True jeśli poprawna pozycja / False jeśli niepoprawna pozycja
     */
    protected boolean isShipPlacedGood(GameObject actualShip, int boardNumber) {
        // Checking if ship is dropped on good position not colliding with anything
        if (boardNumber == 1) {
            Rectangle board = new Rectangle(FirstBoardStart.x, FirstBoardStart.y, BOX_WIDTH_F * BOX_X_AXIS_NUMBER,
                    BOX_HEIGHT_F * BOX_Y_AXIS_NUMBER);
            if (board.contains(actualShip.alligmentRectangle)) {
                for (int i = 0; i < sum; i++) {
                    if (actualShip == FirstBoardShipsSprites[i])
                        continue;
                    // Need change Work in progress But working great actually
                    boolean actualShipRotatedVertically = actualShip.rotation % 2 == 1;
                    boolean otherShipRotatedVertically = FirstBoardShipsSprites[i].rotation % 2 == 1;
                    //
                    if (actualShipRotatedVertically != otherShipRotatedVertically) {
                        if (actualShip.collide(FirstBoardShipsSprites[i].alligmentRectangle, true,
                                actualShipRotatedVertically))
                            return false;
                    } else {
                        if (actualShip.collide(FirstBoardShipsSprites[i].alligmentRectangle))
                            return false;
                    }
                }
                return true;
            } else
                return false;
        } else {
            Rectangle board = new Rectangle(SecondBoardStart.x, SecondBoardStart.y, BOX_WIDTH_F * BOX_X_AXIS_NUMBER,
                    BOX_HEIGHT_F * BOX_Y_AXIS_NUMBER);
            if (board.contains(actualShip.alligmentRectangle)) {
                for (int i = 0; i < sum; i++) {
                    if (actualShip == SecondBoardShipsSprites[i])
                        continue;
                    // Need change Work in progress But working great actually
                    boolean actualShipRotatedVertically = actualShip.rotation % 2 == 1;
                    boolean otherShipRotatedVertically = SecondBoardShipsSprites[i].rotation % 2 == 1;
                    //
                    if (actualShipRotatedVertically != otherShipRotatedVertically) {
                        if (actualShip.collide(SecondBoardShipsSprites[i].alligmentRectangle, true,
                                actualShipRotatedVertically))
                            return false;
                    } else {
                        if (actualShip.collide(SecondBoardShipsSprites[i].alligmentRectangle))
                            return false;
                    }
                }
                return true;
            } else
                return false;
        }
    }

    /**
     * Metoda do rotowania aktualnie trzymanego statku po wciśnięciu klawisza R
     */
    protected void rotateActualShip() {
        FirstBoardShipsSprites[activeSpriteDrag].rotate90();
    }

    /**
     * Metoda do rysowania tekstu pomocy w czasie przed bitwą
     * 
     * @param font  Czcionka do tekstu
     * @param batch SpriteBatch do rysowania na ekranie
     */
    protected void drawStage2Text(BitmapFont font, SpriteBatch batch) {
        String text = "Place your ships within the board !";
        int len = text.length();
        font.draw(batch, text, (gameWidth_f - 200 - (43 * (len / 2))), gameHeight_f / 2 + 200);
        text = "Confirm it by clicking READY button !";
        len = text.length();
        font.draw(batch, text, (gameWidth_f - 180 - (43 * (len / 2))), gameHeight_f / 2 + 100);
        text = "Press R to rotate current ship!";
        len = text.length();
        font.draw(batch, text, (gameWidth_f - 230 - (43 * (len / 2))), gameHeight_f / 2);
    }

    /**
     * Metoda do sprawdzenia czy wszystkie statki są na dobrych pozycjach
     * 
     * @return boolean Zwraca true jeśli wszystkie są dobrze ustawione
     */
    protected boolean checkAllShips() {
        for (int i = 0; i < sum; i++) {
            if (isShipPlacedGood(FirstBoardShipsSprites[i], 1) == false)
                return false;
        }
        return true;
    }

    /**
     * Metoda do obracania wieżyczkami podczas własnej tury
     * 
     * @param screenX Pozycja X myszki na planszy wroga
     * @param screenY Pozycja Y myszki na planszy wroga
     */
    // Stage 3 later
    protected void rotateTurretsWithMouse(float screenX, float screenY) {
        screenY = gameHeight_f - screenY;
        float angle;

        if (PlayerTurn == 1) {
            for (int j = 0; j < sum; j++) {
                GameObject actualShip = FirstBoardShipsSprites[j];
                if (actualShip.shipDestroyed == true)
                    continue;
                for (int i = 0; i < actualShip.turretsAmmount; i++) {
                    Vector2f turretPos = actualShip.getVectorPos(i);
                    angle = MathUtils.radiansToDegrees * MathUtils.atan2(screenX - turretPos.x, turretPos.y - screenY);
                    if (angle < 0)
                        angle += 360;
                    switch (actualShip.rotation) {
                    case 0:
                        break;
                    case 1:
                        angle += 90;
                        break;
                    case 2:
                        angle += 180;
                        break;
                    case 3:
                        angle += 270;
                        break;
                    }
                    actualShip.rotateTurret(angle, i);
                }
            }

        } else {
            for (int j = 0; j < sum; j++) {
                GameObject actualShip = SecondBoardShipsSprites[j];
                if (actualShip.shipDestroyed == true)
                    continue;
                for (int i = 0; i < actualShip.turretsAmmount; i++) {
                    Vector2f turretPos = actualShip.getVectorPos(i);
                    angle = MathUtils.radiansToDegrees * MathUtils.atan2(screenX - turretPos.x, turretPos.y - screenY);
                    if (angle < 0)
                        angle += 360;
                    switch (actualShip.rotation) {
                    case 0:
                        break;
                    case 1:
                        angle += 90;
                        break;
                    case 2:
                        angle += 180;
                        break;
                    case 3:
                        angle += 270;
                        break;
                    }
                    actualShip.rotateTurret(angle, i);
                }
            }
        }
    }

    /**
     * Metoda do sprawdzenia czy trafiono w jakiś okręt na planszach
     * 
     * @param xPos Pozycja x jako indeks poziomy w tablicy
     * @param yPos Pozycja y jako indeks pionowy w tablicy
     */
    protected void checkHit(int xPos, int yPos) {
        int tx = xPos, ty = yPos;
        switch (PlayerTurn) {
        case 1:
            if (secondBoard.ShipsPlaced[xPos][yPos] == 1) {
                FirstPlayerShotsDone[xPos][yPos] = 1;
                PlayerOne.increaseCombo();
                PlayerOne.addPointsForHit();
                hitted = true;
                missed = false;
                xPos *= BOX_WIDTH_F;
                xPos += SecondBoardStart.x + 32;
                yPos *= BOX_HEIGHT_F;
                yPos += SecondBoardStart.y + 32;
                hitPos.set(xPos, yPos);
                hitEffect.setPos(hitPos);
            } else {
                PlayerOne.zeroCombo();
                missed = true;
                hitted = false;
                xPos *= BOX_WIDTH_F;
                xPos += SecondBoardStart.x + 32;
                yPos *= BOX_HEIGHT_F;
                yPos += SecondBoardStart.y + 32;
                missPos.set(xPos, yPos);
                missEffect.setPos(missPos);
            }
            if (hitted) {
                secondBoard.hitShip(tx, ty);
            }

            break;
        case 2:
            if (firstBoard.ShipsPlaced[xPos][yPos] == 1) {
                SecondPlayerShotsDone[xPos][yPos] = 1;
                PlayerTwo.increaseCombo();
                PlayerTwo.addPointsForHit();
                hitted = true;
                missed = false;
                xPos *= BOX_WIDTH_F;
                xPos += FirstBoardStart.x + 32;
                yPos *= BOX_HEIGHT_F;
                yPos += FirstBoardStart.y + 32;
                hitPos.set(xPos, yPos);
                hitEffect.setPos(hitPos);
            } else {
                PlayerTwo.zeroCombo();
                missed = true;
                hitted = false;
                xPos *= BOX_WIDTH_F;
                xPos += FirstBoardStart.x + 32;
                yPos *= BOX_HEIGHT_F;
                yPos += FirstBoardStart.y + 32;
                missPos.set(xPos, yPos);
                missEffect.setPos(missPos);
            }
            if (hitted) {
                firstBoard.hitShip(tx, ty);
            }
            break;
        }
    }

    /**
     * Metoda do oddawania strzałów
     * 
     * @param screenX Pozycja x myszki na planszy wroga / lub indeks x w tablicy
     *                jeśli komputer strzela
     * @param screenY Pozycja y myszki na planszy wroga / lub indeks y w tablicy
     *                jeśli komputer strzela
     * @return boolean Zwraca true jeśli strzelono
     */
    protected boolean shoot(int screenX, int screenY) {
        if (shootingEnabled) {
            int xPos, yPos;
            switch (PlayerTurn) {
            case 1:
                for (int i = 0; i < sum; i++) {
                    if (FirstBoardShipsSprites[i].shipDestroyed)
                        continue;
                    shootEffect[i].setPositions(FirstBoardShipsSprites[i]);
                }
                screenY = (int) gameHeight_f - screenY;
                xPos = (int) ((screenX - SecondBoardStart.x) / BOX_WIDTH_F);
                yPos = (int) ((screenY - SecondBoardStart.y) / BOX_HEIGHT_F);
                FirstPlayerShotsDone[xPos][yPos] = -1;
                shootingEnabled = false;
                Gdx.graphics.setCursor(crosshairs[0]);
                checkHit(xPos, yPos);
                break;
            case 2:
                xPos = screenX;
                yPos = screenY;
                SecondPlayerShotsDone[xPos][yPos] = -1;
                shootingEnabled = false;
                checkHit(xPos, yPos);
                break;
            }
            return true;
        }
        return false;
    }

    /**
     * Metoda do sprawdzenia czy gracz może oddać strzał na daną pozycję na planszy
     * wroga
     * 
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     */
    protected void checkEnemyBoard(int screenX, int screenY) {
        Rectangle board = new Rectangle(SecondBoardStart.x, SecondBoardStart.y, BOX_WIDTH_F * BOX_X_AXIS_NUMBER,
                BOX_HEIGHT_F * BOX_Y_AXIS_NUMBER);
        screenY = (int) gameHeight_f - screenY;
        if (board.contains(screenX, screenY)) {
            if (PlayerTurn == 1) {
                int xPos = (int) ((screenX - SecondBoardStart.x) / BOX_WIDTH_F);
                int yPos = (int) ((screenY - SecondBoardStart.y) / BOX_HEIGHT_F);
                if (xPos == 10 || yPos == 10)
                    return;
                if (FirstPlayerShotsDone[xPos][yPos] != 0) {
                    shootingEnabled = false;
                    rotateEnabled = true;
                    Gdx.graphics.setCursor(crosshairs[0]);
                } else {
                    shootingEnabled = true;
                    rotateEnabled = true;
                    Gdx.graphics.setCursor(crosshairs[1]);
                }
            } else if (PlayerTurn == 2) {
                int xPos = (int) ((screenX - SecondBoardStart.x) / BOX_WIDTH_F);
                int yPos = (int) ((screenY - SecondBoardStart.y) / BOX_HEIGHT_F);
                if (xPos == 10 || yPos == 10)
                    return;
                if (FirstPlayerShotsDone[xPos][yPos] != 0) {
                    shootingEnabled = false;
                    rotateEnabled = true;

                } else {
                    shootingEnabled = true;
                    rotateEnabled = true;

                }
            }

        } else {
            Gdx.graphics.setCursor(crosshairs[2]);
            shootingEnabled = false;
            rotateEnabled = false;
        }
    }

    /**
     * Metoda do zwalniania zasobów wykorzystywanych przez klasę
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
