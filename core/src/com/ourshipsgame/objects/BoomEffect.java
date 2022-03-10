package com.ourshipsgame.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import org.lwjgl.util.vector.Vector2f;

/**
 * Klasa do tworzenia efektów trafień lub nietrafień oraz zniszczeń
 */
public class BoomEffect {
    /**
     * Obiekt przechowujący dźwięk efektu
     */
    private Sound sound;
    /**
     * Obiekt do animowania efektu
     */
    private Animator animator;
    /**
     * Tesktura do sprite'a
     */
    private Texture texture;
    /**
     * Sprite do animacji
     */
    private Sprite sprite;
    /**
     * Zmienna przechowująca wielkość statku / Tylko przy zniszczeniu
     */
    private int radius;
    /**
     * Zmienna przechowująca sprite'y do animacji / Tylko przy zniszczeniu
     */
    private Sprite[] sprites;

    /**
     * Konstruktor klasy tworzący efekt trafienia
     * 
     * @param sound   Dźwięk efektu
     * @param texture Tekstura efektu
     */
    public BoomEffect(Sound sound, Texture texture) {
        this.sound = sound;
        this.animator = new Animator(texture, new Vector2(4, 4), (1.0f / 16f) + 0.015f);
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
        this.sprite.setSize(64, 64);
        this.sprite.setOriginCenter();

    }

    /**
     * Konstruktor klasy tworzący efekt nietrafienia
     * 
     * @param sound      Dźwięk efektu
     * @param texture    Tekstura efektu
     * @param kl         Vector przechowujący klatki animacji
     * @param switchTime Czas zmiany klatki w sekundach
     */
    public BoomEffect(Sound sound, Texture texture, Vector2 kl, float switchTime) {
        this.sound = sound;
        this.animator = new Animator(texture, kl, switchTime + 0.015f);
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
        this.sprite.setSize(64, 64);
        this.sprite.setOriginCenter();

    }

    /**
     * Konstruktor klasy tworzący efekt zniszczenia
     * 
     * @param sound    Dźwięk efektu
     * @param texture  Tekstura efektu
     * @param multiple Parametr dodatkowy określający zniszczenie
     */
    public BoomEffect(Sound sound, Texture texture, boolean multiple) {
        this.sound = sound;
        this.animator = new Animator(texture, new Vector2(4, 4), (1.0f / 16f) + 0.015f);
        this.texture = texture;
        this.sprites = new Sprite[1];
        this.sprites[0] = new Sprite(this.texture);
        this.sprites[0].setSize(64, 64);
        this.sprites[0].setOriginCenter();
    }

    /**
     * Metoda ma za zadanie zwrócić pozycję efektu
     * 
     * @return Vector2
     */
    public Vector2 getPos() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    /**
     * Metoda ustawiająca pozycję efektu
     * 
     * @param vector2f Nowa pozycja efektu
     */
    public void setPos(Vector2f vector2f) {
        this.sprite.setPosition(vector2f.x - 32, vector2f.y - 32);
    }

    /**
     * Metoda tworząca i ustawiająca wiele efektów
     * 
     * @param vector2f Początkowa pozycja efektu
     * @param rotation Rotacja zniszczonego statku
     * @param radius   Wielkość zniszczonego statku
     */
    public void setPos(Vector2f vector2f, int rotation, int radius) {
        this.radius = radius;
        this.sprites = new Sprite[radius];
        for (int i = 0; i < radius; i++) {
            this.sprites[i] = new Sprite(this.texture);
            this.sprites[i].setSize(64, 64);
            this.sprites[i].setOriginCenter();
        }
        if (rotation % 2 == 0) {
            for (int i = 0; i < radius; i++)
                this.sprites[i].setPosition(vector2f.x, vector2f.y + (i * 64));
        } else {
            for (int i = 0; i < radius; i++)
                this.sprites[i].setPosition(vector2f.x + (i * 64), vector2f.y);
        }
    }

    /**
     * Metoda do aktualizacji animacji
     */
    public void updateAnimation() {
        this.animator.update();
        TextureRegion region = animator.getCurrentFrame();
        this.sprite.setRegion(region);
    }

    /**
     * Metoda do aktualizacji animacji przy zniszczeniu
     * 
     * @param multiple Parametr dodatkowy do odróźnienia metod
     */
    public void updateAnimation(boolean multiple) {
        this.animator.update();
        TextureRegion region = animator.getCurrentFrame();
        for (int i = 0; i < radius; i++)
            this.sprites[i].setRegion(region);
    }

    /**
     * Metoda do rysowania efektu na ekranie
     * 
     * @param batch SpriteBatch do rysowania
     */
    public void drawEffect(SpriteBatch batch) {
        this.sprite.draw(batch);
    }

    /**
     * Metoda do rysowania efektów na ekranie
     * 
     * @param batch    SpriteBatch do rysowania
     * @param multiple Parametr dodatkowy do odróźnienia metod
     */
    public void drawEffect(SpriteBatch batch, boolean multiple) {
        for (int i = 0; i < radius; i++)
            this.sprites[i].draw(batch);
    }

    /**
     * Metoda do włączania dźwieku efektów
     * 
     * @param multiple    Parametr dodatkowy do odróźnienia metod
     * @param soundVolume Głośność dźwięku
     * @return boolean Zwraca false po pierwszym odtworzeniu
     */
    public boolean playSound(boolean multiple, float soundVolume) {
        for (int i = 0; i < radius; i++)
            this.sound.play(soundVolume * (1 + (i * 0.1f)));
        return false;
    }

    /**
     * Metoda do włączania dźwięku efektu
     * 
     * @param soundVolume Głośność dźwięku
     */
    public void playSound(float soundVolume) {
        this.sound.play(soundVolume);
    }

    /**
     * Metoda do resetowania animacji
     */
    public void resetAnimation() {
        this.animator.setStartAnimation();
    }
}
