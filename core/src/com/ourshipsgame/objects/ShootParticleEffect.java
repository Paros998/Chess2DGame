package com.ourshipsgame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ourshipsgame.game.GameObject;

/**
 * Klasa ta przechowuje wszystkie efekty strzałów pojedynczego statku
 */
public class ShootParticleEffect {
    /**
     * Tablica sprit'ów przechowuje wszystkie sprite'y wystrzałów
     */
    protected Sprite[] sprites;
    /**
     * Obiekt ten ma za zadanie animować wszystkie sprite'y
     */
    protected Animator animator;
    /**
     * Obiekt przechowujący teksturę strzału
     */
    protected Texture particleTexture;
    /**
     * Zmienne określający pozycję sprite'a
     */
    protected float x, y;
    /**
     * Zmienna określająca ilość wieżyczek statku
     */
    protected int turretsAmmount;

    /**
     * Konstruktor główny obiektu
     * 
     * @param particleTexture Tesktura wystrzału
     * @param x               Pozycja statku w osi x
     * @param y               Pozycja statku w osi y
     * @param vector2         Vector przechowujący ilosc klatek do animacji
     * @param turretsAmmount  Ilość wieżyczek danego statku
     */
    public ShootParticleEffect(Texture particleTexture, float x, float y, Vector2 vector2, int turretsAmmount) {
        this.particleTexture = particleTexture;
        this.sprites = new Sprite[turretsAmmount];
        this.x = x;
        this.y = y;
        this.animator = new Animator(this.particleTexture, vector2, 1 / 64f);
        this.turretsAmmount = turretsAmmount;
        for (int i = 0; i < turretsAmmount; i++) {
            this.sprites[i] = new Sprite(particleTexture);
            this.sprites[i].setScale(0.3f);
            this.sprites[i].setOriginCenter();
        }

    }

    /**
     * Zadaniem tej metody jest poprawne ulokowanie animacji wystrzału zależnie od
     * pozycji i rotacji , statku i wieżyczek.
     * 
     * @param actualShip Obiekt przechowujący dane o statku należącym do obiektu tej
     *                   klasy
     */
    public void setPositions(GameObject actualShip) {
        Sprite[] turrets = actualShip.getTurrets();
        int rotation = actualShip.getRotation();
        for (int i = 0; i < turretsAmmount; i++) {
            float width = turrets[i].getWidth();
            float height = turrets[i].getHeight();
            float x = turrets[i].getX();
            float y = turrets[i].getY();
            float angle = turrets[i].getRotation();
            angle -= rotation * 90;
            if (angle < 0)
                angle += 360;

            float percent = 0;
            if (angle >= 0 && angle <= 90) {
                percent = angle / 90;
                x += (width / 2) + ((height / 2) * percent);
                y -= (height / 2) * percent;
            } else if (angle > 90 && angle <= 180) {
                percent = (angle - 90) / 90;
                x += (width / 2) + (height / 2) - ((height / 2) * percent);
                y -= (height / 2) - ((height / 2) * percent);
            } else if (angle > 180 && angle <= 270) {
                percent = (angle - 180) / 90;
                x += (width / 2);
                x -= (2 * width / 2) * percent;
                y -= (height / 2);
                y += (height / 2) * percent;
            } else if (angle > 270 && angle <= 360) {
                percent = (angle - 270) / 90;
                x -= (width / 2);
                x += (2 * width / 2) * percent;
                y -= (height / 2);
                y += (height / 2) * percent;
            }

            if (rotation % 2 == 0) {
                this.sprites[i].setPosition(x - 58, y - 20);
            } else {
                this.sprites[i].setPosition(x - 50, y - 35);
            }

            if (angle >= 0 && angle < 90) {
                this.sprites[i].flip(false, true);
                this.sprites[i].setY(sprites[i].getY() - 30f);
            } else if (angle >= 90 && angle < 180)
                this.sprites[i].flip(false, false);
            else if (angle >= 180 && angle < 270)
                this.sprites[i].flip(true, false);
            else if (angle >= 270 && angle < 360) {
                this.sprites[i].flip(true, true);
                this.sprites[i].setY(sprites[i].getY() - 30f);
            }
        }
    }

    /**
     * Metoda do aktualizowania animacji wszystkich sprite'ów
     * 
     * @param actualShip Obiekt przechowujący dane o statku należącym do obiektu tej
     *                   klasy
     */
    public void updateAnimation(GameObject actualShip) {
        this.animator.update();
        Sprite[] turrets = actualShip.getTurrets();
        TextureRegion region = this.animator.getCurrentFrame();
        int rotation = actualShip.getRotation();
        for (int i = 0; i < turretsAmmount; i++) {
            this.sprites[i].setRegion(region);
            float angle = turrets[i].getRotation();

            switch (rotation) {
            case 0:
                break;
            case 1:
                angle -= 90;
                break;
            case 2:
                angle -= 180;
                break;
            case 3:
                angle -= 270;
                break;
            }

            if (angle < 0)
                angle += 360;

            if (angle >= 0 && angle < 90)
                this.sprites[i].flip(false, true);
            else if (angle >= 90 && angle < 180)
                this.sprites[i].flip(false, false);
            else if (angle >= 180 && angle < 270)
                this.sprites[i].flip(true, false);
            else if (angle >= 270 && angle < 360)
                this.sprites[i].flip(true, true);
        }
    }

    /**
     * Metoda do rysowania wszystkich animacji
     * 
     * @param batch SpriteBatch wykorzystywany do rysowania na ekranie
     */
    public void drawAnimation(SpriteBatch batch) {
        for (int i = 0; i < turretsAmmount; i++) {
            this.sprites[i].draw(batch);
        }
    }

    /**
     * Metoda do resetowania animacji
     */
    public void resetAnimation() {
        this.animator.setStartAnimation();
    }
}
