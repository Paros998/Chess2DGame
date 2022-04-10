package com.ourshipsgame.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ourshipsgame.Main;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.hud.GameTextButton;

/**
 * Klasa okna pomocy w głównym menu.
 */
public class HelpScreen implements Screen, Constant {

    /**
     * Obiekt silnika libGDX. Rysuje obiekty na ekranie tj. sprite'y czy tekstury.
     */
    private Main game;

    /**
     * Tabela rozmieszczająca obrazki z napisami w oknie.
     * Jest następnie usadzana w tabeli mainTable.
     */
    private Table childTable;

    /**
     * Tabela rozmieszczająca elementy na ekranie.
     * Głównie odseparowywuje tabele childTable i przycisk backButton.
     */
    private Table mainTable;
    
    /**
     * Sprite kursora myszki w grze.
     */
    private Sprite cursor;

    /**
     * Przycisk z libGDX. Wraca do głównego okna menu gry.
     */
    private GameTextButton backButton;

    /**
     * Scena z silnika libGDX. 
     * Sprawia, że elementy w grze są interaktywne oraz rysuje je na ekranie.
     */
    private Stage stage;

    /**
     * Obiekt silnika libGDX. Rysuje obiekty na ekranie tj. sprite'y czy tekstury.
     */
    private SpriteBatch batch;

    /**
    * Czcionka do napisów.
    */
    private BitmapFont font;

    /**
     * Główny i jedyny konstruktor klasy HelpScreen.
     * @param game Obiekt klasy Main.
     */
    public HelpScreen(Main game) {
        this.game = game;
    }

    /**
     * Metoda odpowiedzialna za odświeżanie opreacji w oknie pomocy.
     * @param deltaTime Główny czas silnika libGDX.
     */
    private void update(float deltaTime) {
        game.menuElements.moveMenu(deltaTime);
        stage.act();
    }

    /**
     * Metoda odopwiedzialna za tworzenie, ustawianie i ładowanie elementów w oknie
     * pomocy (libGDX).
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();

        // Loading custom font
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("core/assets/fonts/Raleway-ExtraLightItalic.ttf"));
        parameter.color = Color.GOLD;
        parameter.borderColor = Color.BROWN;
        parameter.size = 20;
        parameter.borderWidth = 2;
        font = generator.generateFont(parameter);

        LabelStyle style = new LabelStyle(font, Color.GOLD);

        backButton = new GameTextButton("Back", 0, 0, game.menuElements.skin, 6, game, 1.3f);

        // Loading chess items
        Sprite king = new Sprite(new Texture("core/assets/chess-pieces/w_king_png_shadow_128px.png"));
        Sprite queen = new Sprite(new Texture("core/assets/chess-pieces/w_queen_png_shadow_128px.png"));
        Sprite rook = new Sprite(new Texture("core/assets/chess-pieces/w_rook_png_shadow_128px.png"));
        Sprite bishop = new Sprite(new Texture("core/assets/chess-pieces/w_bishop_png_shadow_128px.png"));
        Sprite knight = new Sprite(new Texture("core/assets/chess-pieces/w_knight_png_shadow_128px.png"));
        Sprite pawn = new Sprite(new Texture("core/assets/chess-pieces/w_pawn_png_shadow_128px.png"));

        float scaleFactorial = 1.5f;

        king.setSize(king.getWidth() / scaleFactorial, king.getHeight() / scaleFactorial);
        queen.setSize(queen.getWidth() / scaleFactorial, queen.getHeight() / scaleFactorial);
        rook.setSize(rook.getWidth() / scaleFactorial, rook.getHeight() / scaleFactorial);
        bishop.setSize(bishop.getWidth() / scaleFactorial, bishop.getHeight() / scaleFactorial);
        knight.setSize(knight.getWidth() / scaleFactorial, knight.getHeight() / scaleFactorial);
        pawn.setSize(pawn.getWidth() / scaleFactorial, pawn.getHeight() / scaleFactorial);

        cursor = new Sprite(new Texture("core/assets/ui/ui.hud/cursors/test.png"));

        // Creating main Table for buttons and child Table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.left();

        // Creating Table for descriptions and icons
        childTable = new Table();
        // childTable.setFillParent(true);

        // Adding elements to child Table
        Label label;

        childTable.add(new Image(new SpriteDrawable(king))).expandX();
        childTable.row();
        label = new Label(
                "Kings move one square in any direction, so long as that square is not attacked by an enemy piece.",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                "Additionally, kings are able to make a special move, known as castling.",
                style
        );
        childTable.add(label).expandX();

        childTable.row().padTop(30);

        childTable.add(new Image(new SpriteDrawable(queen))).expandX();
        childTable.row();
        label = new Label(
                "Queens move diagonally, horizontally, or vertically any number of squares.",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                "They are unable to jump over pieces.",
                style
        );
        childTable.add(label).expandX();

        childTable.row().padTop(30);

        childTable.add(new Image(new SpriteDrawable(rook))).expandX();
        childTable.row();
        label = new Label(
                "Rooks move horizontally or vertically any number of squares",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                "They are unable to jump over pieces. Rooks move when the king castles.",
                style
        );
        childTable.add(label).expandX();

        childTable.row().padTop(30);

        childTable.add(new Image(new SpriteDrawable(bishop))).expandX();
        childTable.row();
        label = new Label(
                "Bishops move diagonally any number of squares.",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                "They are unable to jump over pieces.",
                style
        );
        childTable.add(label).expandX();

        childTable.row().padTop(30);

        childTable.add(new Image(new SpriteDrawable(knight))).expandX();
        childTable.row();
        label = new Label(
                "Knights move in an ‘L’ shape’: two squares in a horizontal or vertical direction,",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                " then move one square horizontally or vertically.",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                "They are the only piece able to jump over other pieces.",
                style
        );
        childTable.add(label).expandX();

        childTable.row().padTop(30);

        childTable.add(new Image(new SpriteDrawable(pawn))).expandX();
        childTable.row();
        label = new Label(
                "Pawns move vertically forward one square, with the option to move two squares if they have not yet moved.",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                "Pawns are the only piece to capture different to how they move.",
                style
        );
        childTable.add(label).expandX();
        childTable.row();
        label = new Label(
                "The pawns capture one square diagonally in a forward direction.",
                style
        );
        childTable.add(label).expandX();
        childTable.row();

        // Adding elements to main Table
        mainTable.add(childTable).expandX();// .padRight(200);
        mainTable.row();
        mainTable.add(backButton);

        stage.addActor(mainTable);
    }

    /**
     * Metoda odpowiedzialna za renderowanie okna pomocy (libGDX).
     * @param delta Główny czas silnika libGDX.
     */
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(game.menuElements.menuTexture.getTexture(), game.menuElements.menuTexture.x,
                game.menuElements.menuTexture.y);

        batch.end();

        stage.draw();
    }

    /**
     * Metoda obsługująca skalowanie okna gry (libGDX).
     * @param width Szerokość okna.
     * @param height Wysokość okna.
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Metoda obsługująca pauzę w grze (libGDX).
     */  
    @Override
    public void pause() {

    }

    /**
     * Metoda obsługująca wyłączenie pauzy (libGDX).
     */
    @Override
    public void resume() {

    }

    /**
     * Metoda obsługująca ukrycie okna gry (libGDX).
     */
    @Override
    public void hide() {
        dispose();
    }

    /**
     * Metoda obsługująca niszczenie elementów silnika libGDX.
     */
    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();

        //cursor.getTexture().dispose();

        font.dispose();
    }
}
