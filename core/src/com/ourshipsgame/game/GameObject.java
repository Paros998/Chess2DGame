package com.ourshipsgame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.objects.Animator;
import org.lwjgl.util.vector.Vector2f;

/**
 * Klasa przechowująca wszystkie dane dotyczące jednego statku
 */
public class GameObject extends Rectangle implements Constant {
    /**
     * Oznaczenie wersji
     */
    private static final long serialVersionUID = 1L;
    /**
     * Tekstura statku
     */
    protected Texture texture;
    /**
     * Tekstura fal tworzonych przez statek
     */
    protected Texture textureWave;
    /**
     * Tekstury wieżyczek statku
     */
    protected Texture[] turretTextures;
    /**
     * Sprite statku
     */
    protected Sprite sprite;
    /**
     * Sprite fal statku
     */
    protected Sprite spriteWave;
    /**
     * Sprite'y wieżyczek
     */
    protected Sprite[] turretSprites;
    /**
     * Wielkość statku
     */
    protected int size;
    /**
     * Zniszczenia statku
     */
    protected int[] destroyed;
    /**
     * Stara pozycja statku do obliczeń
     */
    protected Vector2 oldPos;
    /**
     * Zmienna określająca czy statek jest w dobrym położeniu
     */
    protected boolean goodPlacement;
    /**
     * Zmienna określająca czy statek uległ zniszczeniu
     */
    protected boolean shipDestroyed;
    /**
     * Prostokąt wokół sprite'a do obliczeń kolizji i logiki
     */
    protected Rectangle alligmentRectangle;
    /**
     * Kolor prostokąta statku określający czy jest dobrze ustawiony
     */
    protected Color rectColour;
    /**
     * Zmienna określająca w którą stronę statek jest obrócony
     */
    protected int rotation;
    /**
     * Animator do animowania fal wytwarzanych przez statek
     */
    protected Animator animator;
    /**
     * Ilość wieżyczek które statek posiada
     */
    protected int turretsAmmount;

    /**
     * Konstruktor obiektu z samą teksturą
     */
    public GameObject(String internalPath, float x, float y) {
        texture = new Texture(internalPath);
        this.setX(x);
        this.setY(y);
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    // Constructor for object with a texture ,sprite and animation for this sprite
    /**
     * Kontruktor obiektu z teksturą ,spritem i animacją sprite'a
     * 
     * @param texture        Tekstura statku.
     * @param x              Nowa pozycja X na ekranie.
     * @param y              Nowa pozycja Y na ekranie.
     * @param createSprite   Czy ma tworzyć sprite'a.
     * @param createAnimator Czy ma tworzyć animator do sprite'a.
     * @param vector         Ilość klatek do animatora.
     */
    public GameObject(Texture texture, float x, float y, boolean createSprite, boolean createAnimator, Vector2 vector) {
        this.texture = texture;
        this.setX(x);
        this.setY(y);
        this.width = texture.getWidth();
        this.height = texture.getHeight();

        if (createAnimator)
            animator = new Animator(texture, vector, 0.25f);
        if (createSprite)
            createSprite(texture);
    }

    // Constructor for object with 2 textures , 2 sprites :one is a ship and the
    // other one are his waves, and setting a size of this ship
    /**
     * Konstruktor obiektu z dwoma tekstrurami i dwoma sprite'ami, po jednym dla
     * statku i jego fal
     * 
     * @param texture        Tekstura statku.
     * @param texture2       Tekstura fal.
     * @param x              Nowa pozycja X na ekranie.
     * @param y              Nowa pozycja Y na ekranie.
     * @param createSprite   Czy ma tworzyć sprite'a.
     * @param vector2         Ilość klatek do animatora.
     */
    public GameObject(Texture texture, Texture texture2, float x, float y, boolean createSprite, int sizeofShip,
            Vector2 vector2) {
        this.texture = texture;
        this.textureWave = texture2;
        this.setX(x);
        this.setY(y);
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        if (createSprite) {
            createSprite(texture, sizeofShip);
            createSpriteWave(textureWave);
            this.animator = new Animator(textureWave, vector2, 0.14f);
        }
    }

    // Constructor for object with 2 textures , 2 sprites :one is a ship and the
    // other one are his waves,also this one is creating a sprite array which is
    // used to manage ships turrets, and setting a size of this ship
    /**
     * Konstruktor obiektu z dwoma tekstrurami i dwoma sprite'ami, po jednym dla
     * statku i jego fal oraz z sprite'ami wieżyczek
     * 
     * @param texture Tekstura statku.
     * @param texture2 Tekstura fal.
     * @param textures Tekstury wieżyczek.
     * @param x Nowa pozycja X na ekranie.
     * @param y Nowa pozycja Y na ekranie.
     * @param createSprite Czy ma tworzyć sprite'a.
     * @param vector2 Ilość klatek do animatora.
     */
    public GameObject(Texture texture, Texture texture2, Texture[] textures, float x, float y, boolean createSprite,
            int sizeofShip, Vector2 vector2) {
        this.texture = texture;
        this.textureWave = texture2;
        this.setX(x);
        this.setY(y);
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        if (createSprite) {
            createSprite(texture, sizeofShip);
            createSpriteWave(textureWave);
            this.animator = new Animator(textureWave, vector2, 0.14f);
            if (size == 3)
                turretsAmmount = 10;
            else if (size == 2)
                turretsAmmount = 4;
            else
                turretsAmmount = 2;
            createTurrets(textures);
        }
    }

    /**
     * Metoda do zwracania wielkości statku
     * 
     * @return int wielkość
     */
    public int getShipSize() {
        return this.size;
    }

    /**
     * Metoda do zwracania kąta obróconej wieżyczki
     * 
     * @param index identyfikator wieżyczki
     * @return float kąt obrotu
     */
    public float getTurretRotation(int index) {
        return turretSprites[index].getRotation();
    }

    /**
     * Metoda do zwracania sprite'ów wieżyczek statku
     * 
     * @return Sprite[] wieżyczki
     */
    public Sprite[] getTurrets() {
        return turretSprites;
    }

    // Method to update simple sprite animation
    /**
     * Metoda do aktualizacji animacji statku
     */
    public void updateAnimation() {
        animator.update(0);
        sprite.setRegion(animator.getCurrentFrame());
    }

    // Method to update ship waves animation and calculate it for rotation of ship
    /**
     * Metoda do aktualizacji animacji fal sprite'a
     */
    public void updateTexture() {
        if (spriteWave != null) {
            this.animator.update(0);
            this.spriteWave.setRegion(this.animator.getCurrentFrame());
            this.spriteWave.setScale(1.5f, 1.5f);

            for (int i = 0; i < rotation; i++)
                this.spriteWave.rotate90(true);

            switch (rotation) {
            case 0: {
                if (size == 3)
                    this.spriteWave.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 1.2f);
                else if (size == 2)
                    this.spriteWave.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 1.3f);
                else
                    this.spriteWave.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 1.5f);
                break;
            }
            case 1: {
                if (size == 3)
                    this.spriteWave.setOrigin(sprite.getWidth() / 1.2f, sprite.getHeight() / 2f);
                else if (size == 2)
                    this.spriteWave.setOrigin(sprite.getWidth() / 1.3f, sprite.getHeight() / 2f);
                else
                    this.spriteWave.setOrigin(sprite.getWidth() / 1.5f, sprite.getHeight() / 2f);
                break;
            }
            case 2: {
                if (size == 3)
                    this.spriteWave.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 4.5f);
                else if (size == 2)
                    this.spriteWave.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 4.5f);
                else
                    this.spriteWave.setOrigin(sprite.getWidth() / 1.7f, sprite.getHeight() / 4.5f);
                break;

            }
            case 3: {
                if (size == 3)
                    this.spriteWave.setOrigin(sprite.getWidth() / 4.2f, sprite.getHeight() / 2f);
                else if (size == 2)
                    this.spriteWave.setOrigin(sprite.getWidth() / 3.4f, sprite.getHeight() / 2f);
                else
                    this.spriteWave.setOrigin(sprite.getWidth() / 2.9f, sprite.getHeight() / 2f);
                break;
            }
            }
        }
    }

    /**
     * Metoda do przesuwania tekstury wg osi X
     * 
     * @param x
     */
    // This method simply moves main sprite texture in x axis
    public void moveTexture(float x) {
        this.x += x;
    }

    /**
     * Metoda do tworzenia sprite'a i innych obiektów potrzebnych do jego poprawnego
     * funkcjonowania w grze
     * 
     * @param texture Tekstura
     */
    // This is a method to create a sprite based on a texture , his allignment
    // rectangle and set size and position to a sprite
    protected void createSprite(Texture texture) {
        this.sprite = new Sprite(texture);
        this.oldPos = new Vector2(x, y);
        this.alligmentRectangle = new Rectangle(x, y, width, height);
        this.rectColour = new Color(1, 0, 0, 1);
        this.sprite.setSize(width, height);
        setSpritePos(this.oldPos);
    }

    /**
     * Metoda do tworzenia sprite'a statku i innych obiektów potrzebnych do jego
     * poprawnego funkcjonowania w grze
     * 
     * @param texture Tekstura statku
     * @param size    Wielkość statku
     */
    // This is a method to create a sprite based on a texture , his allignment
    // rectangle and set size and position to a sprite but also its creating a int
    // array which will be used to represent ship destroyment in future
    protected void createSprite(Texture texture, int size) {
        this.sprite = new Sprite(texture);
        this.alligmentRectangle = new Rectangle(x, y, width, height);
        this.rectColour = new Color(1, 0, 0, 1);
        this.size = size;
        this.destroyed = new int[size];
        this.shipDestroyed = false;
        for (int i = 0; i < size; i++)
            destroyed[i] = 0;
        this.oldPos = new Vector2(x, y);
        this.sprite.setSize(width, height);
        setSpritePos(this.oldPos);
    }

    /**
     * Metoda do tworzenia tablicy sprite'ów wieżyczek statku i ich ustawienia -
     * 
     * @param textures Tekstury wieżyczek
     */
    // This method has to create array of turret sprites and its textures for a ship
    // depending on the ship size and place it on good positions accordingly to the
    // ship type
    protected void createTurrets(Texture[] textures) {

        turretTextures = new Texture[turretsAmmount];
        turretSprites = new Sprite[turretsAmmount];

        switch (turretsAmmount) {
        case 10:
            turretTextures[0] = textures[0];
            turretTextures[1] = textures[1];
            turretTextures[2] = textures[0];
            turretTextures[3] = textures[0];
            turretTextures[4] = textures[2];
            turretTextures[5] = textures[0];
            turretTextures[6] = textures[2];
            turretTextures[7] = textures[3];
            turretTextures[8] = textures[2];
            turretTextures[9] = textures[1];
            break;
        case 4:
            turretTextures[0] = textures[1];
            turretTextures[1] = textures[2];
            turretTextures[2] = textures[1];
            turretTextures[3] = textures[2];
            break;
        case 2:
            turretTextures[0] = textures[2];
            turretTextures[1] = textures[2];
            break;
        }
        if (size == 3) {
            for (int i = 0; i < turretsAmmount; i++) {
                turretSprites[i] = new Sprite(turretTextures[i]);
                turretSprites[i].setPosition(this.sprite.getX() + TurretsPos3[i].x - turretSprites[i].getWidth() / 2,
                        this.sprite.getY() + TurretsPos3[i].y - turretSprites[i].getHeight() / 2);
                turretSprites[i].setOrigin(turretSprites[i].getWidth() / 2, turretSprites[i].getHeight() / 2);

            }
        } else if (size == 2) {
            for (int i = 0; i < turretsAmmount; i++) {
                turretSprites[i] = new Sprite(turretTextures[i]);
                turretSprites[i].setPosition(this.sprite.getX() + TurretsPos2[i].x - turretSprites[i].getWidth() / 2,
                        this.sprite.getY() + TurretsPos2[i].y - turretSprites[i].getHeight() / 2);
                turretSprites[i].setOrigin(turretSprites[i].getWidth() / 2, turretSprites[i].getHeight() / 2);
            }
        } else {
            for (int i = 0; i < turretsAmmount; i++) {
                turretSprites[i] = new Sprite(turretTextures[i]);
                turretSprites[i].setPosition(this.sprite.getX() + TurretsPos1[i].x - turretSprites[i].getWidth() / 2,
                        this.sprite.getY() + TurretsPos1[i].y - turretSprites[i].getHeight() / 2);
                turretSprites[i].setOrigin(turretSprites[i].getWidth() / 2, turretSprites[i].getHeight() / 2);
            }
        }

    }

    /**
     * Metoda do rysowania sprite'ów wieżyczek
     * 
     * @param batch SpriteBatch do rysowania na ekranie
     */
    // this method will draw with a parameter batch every single ship turret if they
    // exist
    public void drawTurrets(SpriteBatch batch) {
        if (this.turretSprites != null)
            for (int i = 0; i < this.turretsAmmount; i++)
                this.turretSprites[i].draw(batch);
    }

    /**
     * Metoda do rysowania wieżyczek statku wroga
     * 
     * @param batch SpriteBatch do rysowania na ekranie
     * @param enemy Określenie czy to wrogi statek
     */
    // this method will draw with a parameter batch every single ship turret if they
    // exist
    public void drawTurrets(SpriteBatch batch, boolean enemy) {
        if (this.shipDestroyed)
            if (this.turretSprites != null)
                for (int i = 0; i < this.turretsAmmount; i++)
                    this.turretSprites[i].draw(batch);
    }

    /**
     * Metoda do zwrócenia tekstury obiektu
     * 
     * @return Texture
     */
    // this method simply return the main sprite texture
    public Texture getTexture() {
        return texture;
    }

    /**
     * Metoda do zwrócenia sprite'a obiektu
     * 
     * @return Sprite
     */
    // this method simply return the main sprite
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Metoda do zwrócenia kierunku obrotu statku
     * 
     * @return int
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Metoda do tworzenia sprite'a fal statku i jego ustawienia
     * 
     * @param texture
     */
    // This method method is simply used for creating the waves sprite for a ship
    // and place it accordingly to the given ship
    protected void createSpriteWave(Texture texture) {
        this.spriteWave = new Sprite(texture);
        this.oldPos = new Vector2(x, y);
        this.alligmentRectangle = new Rectangle(x, y, width, height);
        this.rectColour = new Color(1, 0, 0, 1);
        this.spriteWave.setSize(width, height);
        this.spriteWave.setPosition(oldPos.x, oldPos.y);
        this.spriteWave.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 1.5f);
    }

    /**
     * Metoda do rysowania tekstury
     * 
     * @return Texture
     */
    // this method also returns the main texture but it has different name only
    public Texture drawTexture() {
        return this.texture;
    }

    /**
     * Metoda do rysowania statku wrogiego i jego fal
     * 
     * @param batch SpriteBatch do rysowania na ekranie
     * @param enemy Określenie czy to wrogi okręt
     */
    // this method draws the main sprite and its waves if they exist
    public void drawSprite(SpriteBatch batch, boolean enemy) {
        if (this.shipDestroyed) {
            if (spriteWave != null)
                this.spriteWave.draw(batch);
            this.sprite.draw(batch);
        }
    }

    /**
     * Metoda do rysowania statku i jego fal
     * 
     * @param batch
     */
    // this method draws the main sprite and its waves if they exist
    public void drawSprite(SpriteBatch batch) {
        if (spriteWave != null)
            this.spriteWave.draw(batch);
        this.sprite.draw(batch);

    }

    /**
     * Metoda do rysowania statku i jego fal oraz prostokąta wokół niego
     * 
     * @param batch     SpriteBatch do rysowania na ekranie
     * @param drawRect  Określenie czy należy rysować prostokąt
     * @param drawWaves Określenie czy należy rysować fale statku
     * @param sr        ShapeRenderer do rysowania prostokąta
     * @param enemy     Określenie czy to wrogi statek
     */
    // this method also draws the main sprite and its waves if they exist but its
    // also drawing the main sprite rectangle with good colour based on its
    // placement on a board
    public void drawSprite(SpriteBatch batch, boolean drawRect, boolean drawWaves, ShapeRenderer sr, boolean enemy) {
        if (this.shipDestroyed) {
            if (this.goodPlacement)
                changeRectColour();
            if (drawWaves)
                this.spriteWave.draw(batch);
            if (drawRect) {
                sr.rect(alligmentRectangle.x, alligmentRectangle.y, alligmentRectangle.width, alligmentRectangle.height,
                        rectColour, rectColour, rectColour, rectColour);
            }
            this.sprite.draw(batch);
        }
    }

    /**
     * Metoda do rysowania statku i jego fal oraz prostokąta wokół niego
     * 
     * @param batch     SpriteBatch do rysowania na ekranie
     * @param drawRect  Określenie czy należy rysować prostokąt
     * @param drawWaves Określenie czy należy rysować fale statku
     * @param sr        ShapeRenderer do rysowania prostokąta
     */
    // this method also draws the main sprite and its waves if they exist but its
    // also drawing the main sprite rectangle with good colour based on its
    // placement on a board
    public void drawSprite(SpriteBatch batch, boolean drawRect, boolean drawWaves, ShapeRenderer sr) {
        if (this.goodPlacement)
            changeRectColour();
        if (drawWaves)
            this.spriteWave.draw(batch);
        if (drawRect) {
            sr.rect(alligmentRectangle.x, alligmentRectangle.y, alligmentRectangle.width, alligmentRectangle.height,
                    rectColour, rectColour, rectColour, rectColour);
        }
        this.sprite.draw(batch);

    }

    /**
     * Metoda do ustawienia nowej pozycji statku i wszystkich jego elementów
     * 
     * @param vector2 Nowa pozycja na ekranie
     */
    // this method is used to change the whole gameObject position , its main sprite
    // and also the ship waves sprite and rectangle if they exist
    public void setSpritePos(Vector2 vector2) {
        this.sprite.setPosition(vector2.x, vector2.y);
        if (spriteWave != null)
            this.spriteWave.setPosition(vector2.x, vector2.y);
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(vector2);
        this.x = vector2.x;
        this.y = vector2.y;
        for (int i = 0; i < turretsAmmount; i++)
            if (turretSprites[i] != null) {
                switch (this.rotation) {
                case 0:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos3[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].y - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos2[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].y - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos1[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].y - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 1:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos3[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].x - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos2[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].x - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos1[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].x - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 2:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + this.sprite.getHeight() - TurretsPos3[i].x
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + this.sprite.getWidth() - TurretsPos3[i].y
                                        - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + this.sprite.getHeight() - TurretsPos2[i].x
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + this.sprite.getWidth() - TurretsPos2[i].y
                                        - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + this.sprite.getHeight() - TurretsPos1[i].x
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + this.sprite.getWidth() - TurretsPos1[i].y
                                        - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 3:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos3[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].x - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos2[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].x - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos1[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].x - turretSprites[i].getHeight() / 2);
                    }
                    break;
                }
                turretSprites[i].setOrigin(turretSprites[i].getWidth() / 2, turretSprites[i].getHeight() / 2);
            }
    }

    /**
     * Metoda do ruszenia statku i jego elementów wg osi X i Y
     * 
     * @param vector2 Wartość X i Y o jaką zostanie ruszony statek
     */
    // This method is used to move the whole game object
    // and its second sprite and rectangle if they exist
    // and the ship turrets if they exist
    public void translate(Vector2 vector2) {
        this.sprite.translate(vector2.x, vector2.y);
        if (spriteWave != null)
            this.spriteWave.translate(vector2.x, vector2.y);
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(sprite.getX(), sprite.getY());
        if (turretSprites != null)
            for (int i = 0; i < turretsAmmount; i++)
                turretSprites[i].translate(vector2.x, vector2.y);
        this.x = sprite.getX();
        this.y = sprite.getY();

    }

    /**
     * Metoda do ruszenia statku i jego elementów wg osi X
     * 
     * @param x Wartość X o jaką zostanie ruszony statek
     */
    // This method is used to move the whole game object
    // and its second sprite and rectangle if they exist
    // and the ship turrets if they exist but only in x axis
    public void translateX(float x) {
        this.sprite.translateX(x);
        if (spriteWave != null)
            this.spriteWave.translateX(x);
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(sprite.getX(), this.y);
        if (turretSprites != null)
            for (int i = 0; i < turretsAmmount; i++)
                turretSprites[i].translateX(x);

        this.x = sprite.getX();
    }

    /**
     * Metoda do ruszenia statku i jego elementów wg osi Y
     * 
     * @param y Wartość Y o jaką zostanie ruszony statek
     */
    // This method is used to move the whole game object
    // and its second sprite and rectangle if they exist
    // and the ship turrets if they exist but only in y axis
    public void translateY(float y) {
        this.sprite.translateY(y);
        if (spriteWave != null)
            this.spriteWave.translateY(y);
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(this.x, sprite.getY());
        if (turretSprites != null)
            for (int i = 0; i < turretsAmmount; i++)
                turretSprites[i].translateY(y);
        this.y = sprite.getY();
    }

    /**
     * Metoda do sprawdzenia czy punkt znajduje się w sprite'cie
     * 
     * @param point Punkt do sprawdzenia
     * @return boolean Określenie czy się znajduje w sprite'cie czy nie
     */
    // this method checks if the point is placed in gameobject
    public boolean spriteContains(Vector2 point) {
        if (this.alligmentRectangle.contains(point))
            return true;
        return false;
    }

    // this method is changing the allignment rectangle colour based on good
    // placement on board
    /**
     * Określenie koloru prostokąta w zależności od ustawienia
     */
    public void changeRectColour() {
        if (goodPlacement)
            rectColour = Color.GREEN;
        else if (!goodPlacement)
            rectColour = Color.RED;
    }

    /**
     * Metoda do zmiany wartości określającej czy sprite jest dobrze położony
     * 
     * @param isIt Nowa wartość
     */
    // this method changes the boolean value of good placement
    public void setGoodPlacement(boolean isIt) {
        this.goodPlacement = isIt;
    }

    // this method destroys one element of ship
    /**
     * Metoda do zniszczenia jednego elementu statku
     */
    public void destroyElement() {
        for (int i = 0; i < size; i++)
            if (destroyed[i] == 0) {
                destroyed[i] = 1;
                break;
            }
    }

    /**
     * Metoda do zmiany tekstur na zniszczone tekstury
     * 
     * @param textureD Nowa tekstura statku
     * @param turrets  Nowe tekstury wieżyczek
     */
    public void changeDestroyTexture(Texture textureD, Texture[] turrets) {
        this.spriteWave = null;
        if (this.size == 3) {
            this.sprite.setTexture(textureD);
            turretSprites[0].setTexture(turrets[4]);
            turretSprites[1].setTexture(turrets[5]);
            turretSprites[2].setTexture(turrets[4]);
            turretSprites[3].setTexture(turrets[4]);
            turretSprites[4].setTexture(turrets[6]);
            turretSprites[5].setTexture(turrets[4]);
            turretSprites[6].setTexture(turrets[6]);
            turretSprites[7].setTexture(turrets[7]);
            turretSprites[8].setTexture(turrets[6]);
            turretSprites[9].setTexture(turrets[5]);
        } else if (this.size == 2) {
            this.sprite.setTexture(textureD);
            turretSprites[0].setTexture(turrets[5]);
            turretSprites[1].setTexture(turrets[6]);
            turretSprites[2].setTexture(turrets[5]);
            turretSprites[3].setTexture(turrets[6]);
        } else {
            this.sprite.setTexture(textureD);
            turretSprites[0].setTexture(turrets[6]);
            turretSprites[1].setTexture(turrets[6]);
        }
    }

    // this method checks if the whole ship is destroyed
    /**
     * Metoda do sprawdzenia czy statek jest znisczony
     */
    public void checkDestroyment() {
        for (int i = 0; i < size; i++) {
            if (destroyed[i] == 0)
                return;
        }
        this.shipDestroyed = true;
    }

    /**
     * Metoda do zwracania określenia czy statek jest zniszczony
     * 
     * @return boolean
     */
    // this method return true or false wheter the whole ship is destroyed
    public boolean isDestroyed() {
        return shipDestroyed;
    }

    /**
     * Metoda do sprawdzenia kolizji z innym statkiem na planszy
     * 
     * @param otherRectangle Prostokąt opasający inny okręt
     * @return boolean True jeśli zachodzi kolizja / False jeśli nie zachodzi
     */
    // this method check if this sprite is colliding with another when they have the
    // same rotation
    public boolean collide(Rectangle otherRectangle) {
        float bx = otherRectangle.x;
        float by = otherRectangle.y;
        float bx2 = otherRectangle.width + otherRectangle.x;
        float by2 = otherRectangle.height + otherRectangle.y;
        float ax = alligmentRectangle.x;
        float ay = alligmentRectangle.y;
        float ax2 = alligmentRectangle.width + alligmentRectangle.x;
        float ay2 = alligmentRectangle.height + alligmentRectangle.y;

        if (alligmentRectangle.contains(new Vector2(bx, by)) || alligmentRectangle.contains(new Vector2(bx2, by))
                || alligmentRectangle.contains(new Vector2(bx, by2))
                || alligmentRectangle.contains(new Vector2(bx2, by2)) || otherRectangle.contains(new Vector2(ax, ay))
                || otherRectangle.contains(new Vector2(ax2, ay)) || otherRectangle.contains(new Vector2(ax, ay2))
                || otherRectangle.contains(new Vector2(ax2, ay2)))
            return true;
        else
            return false;
    }

    /**
     * Metoda do sprawdzenia kolizji z innym statkiem na planszy który ma inne
     * obrócenie
     * 
     * @param otherRectangle              Prostokąt opasający inny okręt
     * @param diffRotation                Określenie czy inny statek ma inne
     *                                    obrócenie
     * @param actualShipRotatedVertically Określenie czy ten statek jest obrócony
     *                                    wertykalnie
     * @return boolean True jeśli zachodzi kolizja / False jeśli nie zachodzi
     */
    // this method check if this sprite is colliding with another when they dont
    // have the same rotation
    public boolean collide(Rectangle otherRectangle, boolean diffRotation, boolean actualShipRotatedVertically) {
        float bx = otherRectangle.x;
        float by = otherRectangle.y;
        float bx2 = otherRectangle.width + otherRectangle.x;
        float by2 = otherRectangle.height + otherRectangle.y;
        float ax = alligmentRectangle.x;
        float ay = alligmentRectangle.y;
        float ax2 = alligmentRectangle.width + alligmentRectangle.x;
        float ay2 = alligmentRectangle.height + alligmentRectangle.y;

        if (alligmentRectangle.contains(new Vector2(bx, by)) || alligmentRectangle.contains(new Vector2(bx2, by))
                || alligmentRectangle.contains(new Vector2(bx, by2))
                || alligmentRectangle.contains(new Vector2(bx2, by2)) || otherRectangle.contains(new Vector2(ax, ay))
                || otherRectangle.contains(new Vector2(ax2, ay)) || otherRectangle.contains(new Vector2(ax, ay2))
                || otherRectangle.contains(new Vector2(ax2, ay2)))
            return true;
        else {
            if (actualShipRotatedVertically) {
                if (ax2 < bx || ax > bx2)
                    return false;

                float i = bx += 5;
                while (i < bx2) {
                    if (otherRectangle.contains(new Vector2(i, ay)))
                        return true;
                    i += 5;
                }
                i = ay += 5;
                while (i < ay2) {
                    if (otherRectangle.contains(new Vector2(bx, i)))
                        return true;
                    i += 5;
                }
            } else {
                if (bx2 < ax || bx > ax2)
                    return false;
                float i = ax += 5;
                while (i < ax2) {
                    if (alligmentRectangle.contains(new Vector2(i, by)))
                        return true;
                    i += 5;
                }
                i = by += 5;
                while (i < by2) {
                    if (alligmentRectangle.contains(new Vector2(ax, i)))
                        return true;
                    i += 5;
                }
            }
            return false;
        }
    }

    // this method rotates the whole game object and its components in right for
    // 90degrees
    /**
     * Metoda do obrócenia statku o 90 stopni w prawo oraz wszystkich jego elementów
     */
    public void rotate90() {
        sprite.rotate90(true);
        if (spriteWave != null) {
            spriteWave.rotate90(true);
            this.spriteWave.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 1.5f);
        }

        rotation++;
        if (rotation > 3)
            rotation = 0;

        for (int i = 0; i < turretsAmmount; i++)
            if (turretSprites[i] != null) {

                float tmpHeight = turretSprites[i].getHeight();
                float tmpWidth = turretSprites[i].getWidth();
                turretSprites[i].rotate90(true);
                turretSprites[i].setSize(tmpHeight, tmpWidth);

                switch (rotation) {
                case 0:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos3[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].y - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos2[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].y - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos1[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].y - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 1:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos3[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].x - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos2[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].x - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos1[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].x - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 2:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                (this.sprite.getX() + this.sprite.getHeight()) - TurretsPos3[i].x
                                        - turretSprites[i].getWidth() / 2,
                                (this.sprite.getY() + this.sprite.getWidth()) - TurretsPos3[i].y
                                        - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                (this.sprite.getX() + this.sprite.getHeight()) - TurretsPos2[i].x
                                        - turretSprites[i].getWidth() / 2,
                                (this.sprite.getY() + this.sprite.getWidth()) - TurretsPos2[i].y
                                        - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                (this.sprite.getX() + this.sprite.getHeight()) - TurretsPos1[i].x
                                        - turretSprites[i].getWidth() / 2,
                                (this.sprite.getY() + this.sprite.getWidth()) - TurretsPos1[i].y
                                        - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 3:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos3[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].x - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos2[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].x - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos1[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].x - turretSprites[i].getHeight() / 2);
                    }
                    break;
                }
                turretSprites[i].setOrigin(turretSprites[i].getWidth() / 2, turretSprites[i].getHeight() / 2);
            }

        float tmp = this.height;
        this.height = width;
        this.width = tmp;
        sprite.setSize(width, height);
        spriteWave.setSize(width, height);
        alligmentRectangle.setSize(width, height);
    }

    /**
     * Metoda do ustawienia wieżyczek w odpowiednim miejsu na statku
     */
    public void placeTurretsAccordingly() {
        for (int i = 0; i < turretsAmmount; i++)
            if (turretSprites[i] != null) {

                switch (rotation) {
                case 0:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos3[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].y - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos2[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].y - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos1[i].x - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].y - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 1:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos3[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos3[i].x - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos2[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos2[i].x - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() + TurretsPos1[i].y - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].x - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 2:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                (this.sprite.getX() + this.sprite.getHeight()) - TurretsPos3[i].x
                                        - turretSprites[i].getWidth() / 2 - 128,
                                (this.sprite.getY() + this.sprite.getWidth()) - TurretsPos3[i].y
                                        - turretSprites[i].getHeight() / 2 + 128);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                (this.sprite.getX() + this.sprite.getHeight()) - TurretsPos2[i].x
                                        - turretSprites[i].getWidth() / 2 - 64,
                                (this.sprite.getY() + this.sprite.getWidth()) - TurretsPos2[i].y
                                        - turretSprites[i].getHeight() / 2 + 64);
                    } else {
                        turretSprites[i].setPosition(
                                (this.sprite.getX() + this.sprite.getHeight()) - TurretsPos1[i].x
                                        - turretSprites[i].getWidth() / 2,
                                (this.sprite.getY() + this.sprite.getWidth()) - TurretsPos1[i].y
                                        - turretSprites[i].getHeight() / 2);
                    }
                    break;
                case 3:
                    if (size == 3) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos3[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2 + 128,
                                this.sprite.getY() + TurretsPos3[i].x - turretSprites[i].getHeight() / 2);
                    } else if (size == 2) {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos2[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2 + 64,
                                this.sprite.getY() + TurretsPos2[i].x - turretSprites[i].getHeight() / 2);
                    } else {
                        turretSprites[i].setPosition(
                                this.sprite.getX() - TurretsPos1[i].y + this.sprite.getHeight()
                                        - turretSprites[i].getWidth() / 2,
                                this.sprite.getY() + TurretsPos1[i].x - turretSprites[i].getHeight() / 2);
                    }
                    break;
                }
                turretSprites[i].setOrigin(turretSprites[i].getWidth() / 2, turretSprites[i].getHeight() / 2);
            }
    }

    /**
     * Metoda do obrócenia wieżyczki
     * 
     * @param degrees nowy kąt obrotu
     * @param index   identyfikator wieżyczki
     */
    public void rotateTurret(float degrees, int index) {
        if (!this.shipDestroyed)
            if (this.turretSprites != null) {
                turretSprites[index].setOriginCenter();
                turretSprites[index].setRotation(degrees);
            }
    }

    /**
     * Metoda do zwrócenia pozycji wieżyczki
     * 
     * @param i identyfikator wieżyczki
     * @return Vector2f Pozycja wieżyczki
     */
    public Vector2f getVectorPos(int i) {
        return new Vector2f(turretSprites[i].getX(), turretSprites[i].getY());
    }

    /**
     * Metoda do zwrócenia pozycji statku
     * 
     * @return Vector2f Pozycja statku
     */
    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }

}
