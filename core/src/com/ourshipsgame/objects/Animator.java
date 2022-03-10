package com.ourshipsgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Klasa animująca obiekty w grze.
 */
public class Animator {

    // Data

    /**
     * Obiekt libGDX przechowujący wymiary sprite'a.
     */
    private TextureRegion region;

    /**
     * Wektor dwuwymiarowy, określający obecny obrazek w x i y.
     */
    private Vector2 currentImage;

    /**
     * Wektor dwuwymiarowy, określający wymiary spritesheet.
     */
    private Vector2 imageCount;

    /**
     * Tablica obiektów libGDX przechowująca obrazki do animacji.
     */
    private TextureRegion[][] frames;

    /**
     * Czas przełączania obrazków w animacji.
     */
    private float switchTime;

    /**
     * Maksymalny czas aktulalizowany czasem silnika libGDX.
     */
    private float totalTime;

    // Constructor

    /**
     * Główny i jedyny konstruktor klasy Animator.
     * @param texture Tekstura, spritesheet.
     * @param imageCount Wektor dwuwymiarowy, określający wymiary spritesheet.
     * @param switchTime Czas przełączania obrazków w animacji.
     */
    public Animator(Texture texture, Vector2 imageCount, float switchTime) {
        frames = new TextureRegion[(int) imageCount.y][(int) imageCount.x];
        region = new TextureRegion(texture);
        currentImage = new Vector2(0, 0);
        this.imageCount = imageCount;
        int frameWidth = (int) (region.getRegionWidth() / imageCount.x);
        int frameHeight = (int) (region.getRegionHeight() / imageCount.y);

        for (int j = 0; j < imageCount.y; j++)
            for (int i = 0; i < imageCount.x; i++)
                frames[j][i] = new TextureRegion(region, i * frameWidth, j * frameHeight, frameWidth, frameHeight);

        this.switchTime = switchTime;
        totalTime = 0;
    }

    // Public methods

    /**
     * Metoda resetująca animacje.
     */
    public void setStartAnimation() {
        currentImage.x = currentImage.y = 0;
    }

    /**
     * Metoda aktualizująca animacje.
     */
    public void update() {

        totalTime += Gdx.graphics.getDeltaTime();

        if (totalTime >= switchTime) {
            totalTime -= switchTime;
            currentImage.x++;

            if (currentImage.x >= imageCount.x) {
                currentImage.x = 0;
                currentImage.y++;
            }
            if (imageCount.y <= currentImage.y) {
                currentImage.y = 0;
            }
        }
    }

    
    /** 
     * Metoda aktualizująca animacje w określonym wierszu spritesheet.
     * @param row
     */
    public void update(int row) {
        totalTime += Gdx.graphics.getDeltaTime();
        currentImage.y = row;
        if (totalTime >= switchTime) {
            totalTime -= switchTime;
            currentImage.x++;

            if (currentImage.x >= imageCount.x) {
                currentImage.x = 0;
            }
        }
    }

    
    /** 
     * Metoda typu get, zwracająca obecny obrazek animacji.
     * @return Obecny obrazek animacji.
     */
    public TextureRegion getCurrentFrame() {
        return frames[(int) currentImage.y][(int) currentImage.x];
    }

    
    /** 
     * Metoda typu get, zwracająca animacje spoczynku.
     * @return Animacja spoczynku.
     */
    public TextureRegion getIdleAnimation() {
        return frames[(int) currentImage.y][(int) (imageCount.x - 1)];
    }
}
