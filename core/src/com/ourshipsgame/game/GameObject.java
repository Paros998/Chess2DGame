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
     * Tekstura obiektu
     */
    protected Texture texture;
    /**
     * Sprite statku
     */
    protected Sprite sprite;
    /**
     * Stara pozycja do obliczeń
     */
    protected Vector2 oldPos;
    /**
     * Prostokąt wokół sprite'a do obliczeń kolizji i logiki
     */
    protected Rectangle alligmentRectangle;
    /**
     * Animator do animowania
     */
    protected Animator animator;

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

    // Method to update simple sprite animation
    /**
     * Metoda do aktualizacji animacji statku
     */
    public void updateAnimation() {
        animator.update(0);
        sprite.setRegion(animator.getCurrentFrame());
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
        this.oldPos = new Vector2(x, y);
        this.sprite.setSize(width, height);
        setSpritePos(this.oldPos);
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
     * Metoda do rysowania sprite
     * 
     * @param batch
     */
    // this method draws the main sprite and its waves if they exist
    public void drawSprite(SpriteBatch batch) {
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
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(vector2);
        this.x = vector2.x;
        this.y = vector2.y;
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
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(sprite.getX(), sprite.getY());
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
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(sprite.getX(), this.y);
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
        if (alligmentRectangle != null)
            this.alligmentRectangle.setPosition(this.x, sprite.getY());
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
        return this.alligmentRectangle.contains(point);
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

        return alligmentRectangle.contains(new Vector2(bx, by)) || alligmentRectangle.contains(new Vector2(bx2, by))
                || alligmentRectangle.contains(new Vector2(bx, by2))
                || alligmentRectangle.contains(new Vector2(bx2, by2)) || otherRectangle.contains(new Vector2(ax, ay))
                || otherRectangle.contains(new Vector2(ax2, ay)) || otherRectangle.contains(new Vector2(ax, ay2))
                || otherRectangle.contains(new Vector2(ax2, ay2));
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
