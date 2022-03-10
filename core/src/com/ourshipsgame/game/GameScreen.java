package com.ourshipsgame.game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ourshipsgame.Main;
import com.ourshipsgame.hud.Hud;
import com.ourshipsgame.mainmenu.MenuGlobalElements;
import com.ourshipsgame.mainmenu.MenuScreen;

/**
 * Klasa ekranu głównego gry
 */
public class GameScreen extends GameEngine implements InputProcessor {

    /**
     * Identyfikator klasy
     */
    private final String id = getClass().getName();
    /**
     * AssetManager do ładowania zasobów gry
     */
    public AssetManager manager;
    /**
     * Obiekt aplikacji
     */
    private Main game;
    /**
     * Obiekt ekranu głównego
     */
    private GameScreen GameScreen;
    /**
     * Multiplekser do obsługi wejścia (klawisze/myszka etc)
     */
    private InputMultiplexer inputMultiplexer;
    /**
     * Obiekt głowny interfejsu
     */
    private Hud hud;
    /**
     * Obiekt do rysowania na ekranie
     */
    private SpriteBatch sb;
    /**
     * Obiekt do rysowania kształtów na ekranie
     */
    private ShapeRenderer sr;
    /**
     * Zmienna przechowująca progres ładowania zasobów
     */
    private float progress;
    /**
     * Obiekt do renderowania Tile Mapy gry
     */
    private OrthogonalTiledMapRenderer renderer;
    /**
     * Obiekt określający projekcję gry na ekranie
     */
    private OrthographicCamera camera;
    /**
     * Zmienna określająca ,który to stopień gry do obliczeń logiki gry
     */
    private int gameStage = 1;
    /**
     * Mapa główna gry
     */
    private TiledMap map;
    /**
     * Tło do ekranu ładowania
     */
    private Texture loadingTexture;
    /**
     * Zmienna określająca czy utworzono tekstury
     */
    private boolean createdTextures = false;
    /**
     * Zmienna określająca czy strzelono
     */
    private boolean shootOrder = false;
    /**
     * Zmienna określająca czy dźwięk strzału można włączyć
     */
    private boolean shootSound = false;
    /**
     * Zmienna określająca czy dźwięk trafienia/nietrafienia można włączyć
     */
    private boolean hitMissSound = false;
    /**
     * Zmienna określająca czy należy stworzyć okno dialogowe z użytkownikiem
     */
    private boolean createDialog = false;
    /**
     * Zmienna określająca czy należy renderować na ekranie statki
     */
    private boolean drawShips = true;
    /**
     * Identyfikator dźwieku do ciągłego odtwarzania rotacji wieżyczek
     */
    private long sid;
    /**
     * Warstwy mapy do renderowania w czasie bitwy
     */
    private int[] layers;
    /**
     * Warstwy mapy do renderowania po bitwie
     */
    private int[] endlayers;
    /**
     * Zmienna służąca do aktualizacji logiki związanej z obrotem wieżyczek
     */
    private float rotateTime;

    /**
     * Zmienna służąca do aktualizacji logiki związanej z zniszczeniem okrętu
     */
    private float destroyTime;
    /**
     * Zmienna służąca do aktualizacji logiki związanej oddaniem niecelnego strzału
     */
    private float missTime;
    /**
     * Zmienna służąca do aktualizacji logiki związanej oddaniem celnego strzału
     */
    private float hitTime;
    // other vars
    /**
     * Czcionka do ekranu ładowania
     */
    private BitmapFont font;

    // constructor
    /**
     * Konstruktor ekranu głównego
     * 
     * @param game Obiekt aplikacji
     */
    public GameScreen(Main game) {
        this.GameScreen = this;
        this.game = game;
        Gdx.app.log(id, "This class is loaded!");
        System.out.println(Gdx.app.getJavaHeap() / 1000000);
    }

    // Draw methods
    /**
     * Metoda do renderowania mapy
     */
    private void drawMap() {
        switch (gameStage) {
        case 2:
            renderer.render();
            break;
        case 3:
            renderer.render(layers);
            break;
        case 4:
            renderer.render(endlayers);
            break;
        }
    }

    /**
     * Metoda do renderowania statków i ich elementów
     */
    private void drawShipsEnTurrets() {
        if (drawShips)
            switch (gameStage) {
            case 2:
                for (int i = 0; i < sum; i++) {
                    FirstBoardShipsSprites[i].updateTexture();
                    FirstBoardShipsSprites[i].drawSprite(sb, true, false, sr);
                    FirstBoardShipsSprites[i].drawTurrets(sb);
                    SecondBoardShipsSprites[i].updateTexture();
                    SecondBoardShipsSprites[i].drawSprite(sb, true, false, sr, true);
                    SecondBoardShipsSprites[i].drawTurrets(sb, true);
                }
                break;
            case 3:
                for (int i = 0; i < sum; i++) {
                    FirstBoardShipsSprites[i].updateTexture();
                    FirstBoardShipsSprites[i].drawSprite(sb);
                    FirstBoardShipsSprites[i].drawTurrets(sb);
                    SecondBoardShipsSprites[i].updateTexture();
                    SecondBoardShipsSprites[i].drawSprite(sb, true);
                    SecondBoardShipsSprites[i].drawTurrets(sb, true);
                }
                break;
            case 4:
                for (int i = 0; i < sum; i++) {
                    FirstBoardShipsSprites[i].drawSprite(sb);
                    FirstBoardShipsSprites[i].drawTurrets(sb);
                    SecondBoardShipsSprites[i].drawSprite(sb);
                    SecondBoardShipsSprites[i].drawTurrets(sb);
                }
                break;
            }
    }

    /**
     * Metoda do renderowania efektu trafienia
     * 
     * @param deltaTime czas między klatkami
     */
    private void drawHit(float deltaTime) {
        hitTime += deltaTime;
        if (hitTime <= 1f) {
            hitEffect.updateAnimation();
            hitEffect.drawEffect(sb);
        } else {
            hitEffect.resetAnimation();
            hitTime = 0f;
            hitted = false;
        }
        hitMissSound = false;
    }

    /**
     * Metoda do renderowania efektu nietrafienia
     * 
     * @param deltaTime czas między klatkami
     */
    private void drawMiss(float deltaTime) {
        missTime += deltaTime;
        if (missTime <= 1f) {
            missEffect.updateAnimation();
            missEffect.drawEffect(sb);
        } else {
            missEffect.resetAnimation();
            missTime = 0f;
            missed = false;
        }
        hitMissSound = false;
    }

    /**
     * Metoda do renderowania efektu zniszczenia okrętu
     * 
     * @param deltaTime czas między klatkami
     */
    private void drawDestroyment(float deltaTime) {
        destroyTime += deltaTime;
        if (destroyTime <= 1f) {
            destroymentEffect.updateAnimation(true);
            destroymentEffect.drawEffect(sb, true);
        } else {
            destroymentEffect.resetAnimation();
            destroyed = false;
            destroymentSound = false;
            destroyTime = 0f;
        }
    }

    /**
     * Metoda do renderowania efektu wystrzału okrętów niezniszczonych
     * 
     * @param deltaTime czas między klatkami
     */
    private void drawShootingEffect(float deltaTime) {
        shootingDone = false;
        shootTime += deltaTime;
        if (shootTime <= 1f) {
            shootingEnabled = false;
            if (PlayerTurn == 1) {
                for (int i = 0; i < sum; i++) {
                    if (FirstBoardShipsSprites[i].shipDestroyed)
                        continue;
                    shootEffect[i].updateAnimation(FirstBoardShipsSprites[i]);
                    shootEffect[i].drawAnimation(sb);
                }
            }
        } else if (shootTime > 1f) {
            for (int i = 0; i < sum; i++) {
                if (FirstBoardShipsSprites[i].shipDestroyed)
                    continue;
                shootEffect[i].resetAnimation();
            }
            rotateEnabled = true;
            shootTime = 0f;
            shootOrder = false;
            shootingDone = true;
        }
        if (missed && shootingDone)
            switchTurn();
    }

    /**
     * Metoda do renderowania znaczków pomocniczych dla gracza na planszy wroga
     * 
     * @param batch SpriteBatch do renderowania
     */
    private void drawMarks(SpriteBatch batch) {
        if (gameStage == 3) {
            float xpos, ypos;
            if (PlayerTurn == 1) {
                if (!shootOrder && !destroyed)
                    for (int i = 0; i < 10; i++)
                        for (int j = 0; j < 10; j++) {
                            if (FirstPlayerShotsDone[i][j] == -1) {
                                xpos = i * 64f + SecondBoardStart.x;
                                ypos = j * 64f + SecondBoardStart.y;
                                batch.draw(shootMarks[0], xpos, ypos);
                            } else if (FirstPlayerShotsDone[i][j] == 1) {
                                xpos = i * 64f + SecondBoardStart.x;
                                ypos = j * 64f + SecondBoardStart.y;
                                batch.draw(shootMarks[1], xpos, ypos);
                            }
                        }
            } else
                for (int i = 0; i < 10; i++)
                    for (int j = 0; j < 10; j++) {
                        if (FirstPlayerShotsDone[i][j] == -1) {
                            xpos = i * 64f + SecondBoardStart.x;
                            ypos = j * 64f + SecondBoardStart.y;
                            batch.draw(shootMarks[0], xpos, ypos);
                        } else if (FirstPlayerShotsDone[i][j] == 1) {
                            xpos = i * 64f + SecondBoardStart.x;
                            ypos = j * 64f + SecondBoardStart.y;
                            batch.draw(shootMarks[1], xpos, ypos);
                        }
                    }
        }
    }

    /**
     * Metoda do stworzenia okna dialogowego po skończonej bitwie
     */
    private void createDialog() {
        new Dialog("Do you wish to save score?", hud.getSkin()) {
            {
                this.button("Yes", "Yes");
                this.button("No", "No");
            }

            @Override
            protected void result(Object object) {
                switch (object.toString()) {
                case "Yes":
                    File file = new File("scores.txt");
                    if (!file.exists())
                        try {
                            file.createNewFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    try {
                        FileWriter writer = new FileWriter(file, true);
                        writer.write(PlayerOne.getPlayerName() + " ");
                        writer.write(Float.toString(PlayerOne.getScoreValue()) + " ");
                        writer.write(Float.toString(PlayerOne.getTimeElapsed()) + " ");
                        writer.write(Float.toString(PlayerOne.getAccuracyRatio()) + " ");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case "No":
                    new Dialog("What now?", hud.getSkin()) {
                        {

                            this.button("Main menu!", "menu");
                            this.button("Exit game!", "game");
                        }

                        @Override
                        protected void result(Object object) {
                            switch (object.toString()) {
                            case "menu":
                                GameScreen.dispose();
                                game.menuElements = new MenuGlobalElements(game);
                                game.setScreen(new MenuScreen(game));
                                break;
                            case "game":
                                Gdx.app.exit();
                                break;
                            }
                        }
                    }.show(hud.getStage());
                    break;
                }
            }
        }.show(hud.getStage());
    }

    /**
     * Metoda do renderowania informacji o wynikach gracza i komputera
     * 
     * @param batch SpriteBatch do renderowania
     */
    private void drawScores(SpriteBatch batch) {
        PlayerOne.drawInfo(hudFont, batch, gameWidth_f, gameHeight_f, FirstBoardThreeShipsLeft, FirstBoardTwoShipsLeft,
                FirstBoardOneShipsLeft, shipIcons);
        PlayerTwo.drawInfo(hudFont, batch, gameWidth_f, gameHeight_f, SecondBoardThreeShipsLeft,
                SecondBoardTwoShipsLeft, SecondBoardOneShipsLeft, shipIcons);
        switch (PlayerTurn) {
        case 1:
            turnFontActive.draw(batch, "Your Turn!", gameWidth_f / 2 - 250, gameHeight_f - 140);
            turnFont.draw(batch, "Enemy Turn!", gameWidth_f / 2 + 90, gameHeight_f - 140);
            break;
        case 2:
            turnFont.draw(batch, "Your Turn!", gameWidth_f / 2 - 250, gameHeight_f - 140);
            turnFontActive.draw(batch, "Enemy Turn!", gameWidth_f / 2 + 90, gameHeight_f - 140);
            break;
        }
    }

    /**
     * Metoda do renderowania ekranu ładowania
     */
    private void drawLoadingScreen() {
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
    private void drawExitScreen() {
        String msg;
        if (PlayerOneLost) {
            msg = "You 've Lost!! Better luck next time!";
            font.draw(sb, msg, (gameWidth_f / 2) - 350, gameHeight_f / 2 + 400);
        } else if (PlayerTwoLost) {
            msg = "You 've Won!! Keep it up!!";
            font.draw(sb, msg, (gameWidth_f / 2) - 250, gameHeight_f / 2 + 400);
        }
    }

    // Create methods
    /**
     * Metoda do utworzenia mapy z AssetManagera oraz innych zmiennych do jej
     * funkcjonowania
     */
    private void createMap() {
        map = (TiledMap) manager.get("core/assets/map/mp1.tmx");
        camera = new OrthographicCamera();
        renderer = new OrthogonalTiledMapRenderer(map);
        camera.setToOrtho(false, gameWidth_f, gameHeight_f);

        // Map layers
        layers = new int[4];
        layers[0] = 0;
        layers[1] = 1;
        layers[2] = 2;
        layers[3] = 3;

        endlayers = new int[2];
        endlayers[0] = 0;
        endlayers[1] = 1;
    }

    /**
     * Metoda do utworzenia czcionek
     */
    private void createFonts() {
        font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("core/assets/fonts/Raleway-ExtraLightItalic.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
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

    // method to create elements
    /**
     * Metoda do tworzenia wszystkich elementów graficznych gry
     */
    private void createGraphics() {
        // Map ,textures,cameras
        createMap();
        // changing game stage from loading to Placing ships
        if (preparation(true, manager)) {
            gameStage = 2;
            hud = new Hud(manager, game, GameScreen, crosshairs[2]);
            createdTextures = true;
        }

        hud.gameSettings = game.menuElements.gameSettings;
        // Deleting GlobalMenuElements object
        game.menuElements = null;
        hud.getPlayButton().addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                readyButtonCheck();
                if (gameStage == 3) {
                    hud.getStage().getActors().pop();
                    hud.getPlayersSetNameDialog().hide();
                    PlayerOne.setPlayerName(hud.getPlayersName());
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        hud.getRepeatButton().addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                generateAndPlaceShipsOnBoard(1, true);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    // loading method
    /**
     * Metoda do ładowania wszystkich zasobów gry
     */
    private void loadAssets() {
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("core/assets/map/mp1.tmx", TiledMap.class);
        loadGameEngine(manager);
        loadHudAssets(manager);
    }

    // Sound effects methods
    /**
     * Metoda do odtwarzania dźwięku obrotu wieżyczek
     */
    private void startRotateSound() {
        sid = rotateSound.loop(hud.gameSettings.soundVolume * (1 + 0.5f));
        rotateSound.pause();
    }

    /**
     * Metoda do odtwarzania dźwięków wystrzałów
     */
    private void playShootSound() {
        if (shootTime <= 1f) {
            if (PlayerTurn == 1) {
                int j = 0;
                for (int i = 0; i < sum; i++)
                    if (!FirstBoardShipsSprites[i].shipDestroyed)
                        j++;
                for (int i = 0; i < j; i++)
                    ShootSounds[i].play(hud.gameSettings.soundVolume);
            } else {
                int j = 0;
                for (int i = 0; i < sum; i++)
                    if (!SecondBoardShipsSprites[i].shipDestroyed)
                        j++;
                for (int i = 0; i < j; i++)
                    ShootSounds[i].play(hud.gameSettings.soundVolume);
            }
        }
        shootSound = false;
    }

    // Invoked after ready button is pressed in stage 2
    /**
     * Metoda do sprawdzenia czy statki są na dobrych pozycjach i wystartowanie
     * bitwy
     */
    public void readyButtonCheck() {
        if (checkAllShips()) {
            firstBoard.placeShipOnBoard(sum);
            // secondBoard.placeShipOnBoard(sum);
            gameStage = 3;
            startRotateSound();
        }
    }

    /**
     * Metoda do aktualizacji logiki gry
     * 
     * @param deltaTime czas między klatkami
     */
    // update logics of game
    private void update(float deltaTime) {

        if (gameStage == 2)
            if (checkAllShips())
                hud.getPlayButton().getStyle().imageUp = new SpriteDrawable(hud.getPlayButtonGreenStyle());

        if (FirstBoardShipsDestroyed == sum) {
            gameStage = 4;
            PlayerOneLost = true;
        } else if (SecondBoardShipsDestroyed == sum) {
            gameStage = 4;
            PlayerTwoLost = true;
        }

        if (gameStage == 4)
            if (!createDialog) {
                if (PlayerOneLost)
                    endSounds[1].play(hud.gameSettings.soundVolume);
                else if (PlayerTwoLost)
                    endSounds[0].play(hud.gameSettings.soundVolume);
                createDialog();
                createDialog = true;
                Gdx.graphics.setCursor(crosshairs[2]);
            }

        rotateTime += deltaTime;
        if (rotateTime >= 0.3f) {
            rotateTime -= 0.32f;
            rotateSound.pause();
        }

        rotateSound.setVolume(sid, hud.gameSettings.soundVolume * (1 + 0.5f));
        if (gameStage == 3) {

            if (PlayerTurn == 1)
                PlayerOne.updateTime(deltaTime);
            else
                PlayerTwo.updateTime(deltaTime);
            // Update AI info
            if (shootOrder)
                return;
            else {
                if (PlayerTurn == 2) {
                    Gdx.graphics.setCursor(crosshairs[2]);
                    if (enemyComputerPlayerAi != null) {
                        shootingEnabled = true;
                        shootOrder = enemyComputerPlayerAi.attackEnemy(deltaTime);
                        if (shootOrder) {
                            shoot((int) enemyComputerPlayerAi.getX(), (int) enemyComputerPlayerAi.getY());
                            shootSound = true;
                            hitMissSound = true;
                            enemyComputerPlayerAi.update(missed, hitted, destroyed, SecondPlayerShotsDone,
                                    FirstBoardShipsSprites, FirstBoardStart, sum);
                            float x, y;
                            x = (enemyComputerPlayerAi.getX() * 64.0f) + FirstBoardStart.x;
                            y = (enemyComputerPlayerAi.getY() * 64.0f) + FirstBoardStart.y;
                            rotateTurretsWithMouse(x, y);
                            PlayerTwo.update(SecondPlayerShotsDone);
                            shootingEnabled = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Metoda do renderowania całej szaty graficznej gry
     * 
     * @param deltaTime czas między klatkami
     */
    // game loop method
    @Override
    public void render(float deltaTime) {
        // buffer screen
        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (manager.update()) {
            // When loading screen disappers
            if (createdTextures == false) {
                loadingTexture.dispose();
                createGraphics();
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(this);
                inputMultiplexer.addProcessor(hud.getStage());
                Gdx.input.setInputProcessor(inputMultiplexer);
                // tmp
            }
            if (hud.isPasued())
                Gdx.input.setInputProcessor(hud.getStage());
            else
                Gdx.input.setInputProcessor(inputMultiplexer);

            // Map update and tilemap render
            camera.update();
            renderer.setView(camera);
            drawMap();
            // update
            update(deltaTime);
            // render things below
            sb.begin();
            sr.setAutoShapeType(true);
            sr.begin();
            // Do not place any drawings up!!

            // Ships // Turrets
            drawShipsEnTurrets();

            // Texts
            switch (gameStage) {
            case 2:
                drawStage2Text(font, sb);
                break;
            case 3:
                drawScores(sb);
                drawMarks(sb);
                if (shootOrder) {
                    shootingEnabled = false;
                    rotateEnabled = false;
                    if (shootSound) {
                        playShootSound();
                    }
                    drawShootingEffect(deltaTime);
                    if (hitted == true && destroyed == false) {
                        if (hitMissSound)
                            hitEffect.playSound(hud.gameSettings.soundVolume);
                        drawHit(deltaTime);
                    }
                    if (missed) {
                        if (hitMissSound)
                            missEffect.playSound(hud.gameSettings.soundVolume);
                        drawMiss(deltaTime);
                    }

                }
                if (destroyed) {
                    missed = false;
                    hitMissSound = false;
                    shootSound = false;
                    if (destroymentSound)
                        destroymentSound = destroymentEffect.playSound(true, hud.gameSettings.soundVolume);
                    drawDestroyment(deltaTime);
                }
                break;
            case 4:
                drawExitScreen();
                break;
            }

            sb.end();
            sr.end();
            hud.update();
        } else {
            // While loading the game assets
            drawLoadingScreen();
        }
    }

    /**
     * Metoda wywoływana po pokazaniu ekranu gry
     */
    @Override
    public void show() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        manager = new AssetManager();
        loadingTexture = new Texture("core/assets/backgroundtextures/paperTextOld.png");
        createFonts();
        loadAssets();
    }

    /**
     * Metoda wywoływana gdy ekran jest zapauzowany
     */
    @Override
    public void pause() {
        super.pause();
    }

    /**
     * Metoda wywoływania gdy ekran wraca do aktywności
     */
    @Override
    public void resume() {
        super.resume();
    }

    /**
     * Metoda wywoływana gdy rozmiar okna jest zmieniany
     * 
     * @param width  Nowa szerokość okna
     * @param height Nowa wysokość okna
     */
    @Override
    public void resize(int width, int height) {
        gameHeight = height;
        gameWidth = width;
        gameHeight_f = (float) gameHeight;
        gameWidth_f = (float) gameWidth;
        super.resize(width, height);
    }

    /**
     * Metoda wywoływana gdy ekran zwalnia wszystkie zasoby
     */
    @Override
    public void dispose() {
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

    /**
     * Metoda wywoływana gdy ekran jest chowany
     */
    @Override
    public void hide() {
        super.hide();
    }

    /**
     * Metoda wywoływana gdy klawisz jest wciskany
     * 
     * @param keycode Oznaczenie klawisza
     * @return boolean
     */
    @Override
    public boolean keyDown(int keycode) {
        if (gameStage == 2) {
            if (Gdx.input.isKeyPressed(Keys.R))
                if ((activeSpriteDrag <= sum - 1) && (activeSpriteDrag >= 0))
                    rotateActualShip();
        }
        return false;
    }

    /**
     * Metoda wywoływana gdy klawisz jest puszczany
     * 
     * @param keycode Oznaczenie klawisza
     * @return boolean
     */
    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    /**
     * Metoda wywoływana gdy znak klawiatury zostanie wciśnięty
     * 
     * @param character Znak
     * @return boolean
     */
    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    /**
     * Metoda wywoływana gdy klawisz myszki zostanie wciśnięty
     * 
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @param pointer Wskaźnik na coś..
     * @param button  Oznaczenie przycisku który został wciśnięty
     * @return boolean
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
            if (gameStage == 2)
                touchDownSprite(screenX, screenY);
            if (gameStage == 3) {
                if (PlayerTurn == 1) {
                    if (shootingDone) {
                        shootOrder = shoot(screenX, screenY);
                        if (shootOrder) {
                            PlayerOne.update(FirstPlayerShotsDone);
                            shootSound = true;
                            hitMissSound = true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Metoda wywoływana gdy klawisz myszki zostanie upuszczony
     * 
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @param pointer Wskaźnik na coś..
     * @param button  Oznaczenie przycisku który został wciśnięty
     * @return boolean
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (gameStage == 2)
            touchUpSprite();
        return false;
    }

    /**
     * Metoda wywoływana gdy klawisz myszki został wciśnięty i ruszony po ekranie
     * 
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @param pointer Wskaźnik na coś..
     * @return boolean
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (gameStage == 2)
            dragSprite(screenX, screenY);
        return false;
    }

    /**
     * Metoda wywoływana gdy mysz zostanie ruszona na ekranie
     * 
     * @param screenX Pozycja X myszki na ekranie
     * @param screenY Pozycja Y myszki na ekranie
     * @return boolean
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (gameStage == 3) {
            if (PlayerTurn == 1) {
                if (shootingDone) {
                    if (!shootOrder)
                        checkEnemyBoard(screenX, screenY);
                    if (rotateEnabled) {
                        rotateSound.resume();
                        rotateTurretsWithMouse(screenX, screenY);
                    }
                }
            }

        }
        return false;
    }

    /**
     * Metoda wywoływana gdy scroll myszki będzie aktywowany
     * 
     * @param amountX ilość obrotu w poziomie
     * @param amountY ilość obrotu w pionie
     * @return boolean
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
