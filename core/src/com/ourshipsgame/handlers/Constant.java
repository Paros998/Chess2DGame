package com.ourshipsgame.handlers;

import org.lwjgl.util.vector.Vector2f;

/**
 * Interfejs przechowujący kilka stałych do obliczeń itp
 */
public interface Constant {
    // Rozdzielczosc ekranu
    /**
     * Ilość pikseli okna w poziomie
     */
    int GAME_WIDTH = 1920;
    /**
     * Ilość pikseli okna w pionie
     */
    int GAME_HEIGHT = 1080;
    /**
     * Ilość pikseli okna w poziomie
     */
    float GAME_WIDTH_F = 1920.0f;
    /**
     * Ilość pikseli okna w pionie
     */
    float GAME_HEIGHT_F = 1080.0f;
    // Rozmiary kwadratów na planszy
    /**
     * Szerokość jednej kratki
     */
    int BOX_WIDTH = 64;
    /**
     * Wysokość jednej kratki
     */
    int BOX_HEIGHT = 64;
    /**
     * Szerokość jednej kratki
     */
    float BOX_WIDTH_F = 64.0f;
    /**
     * Wysokość jednej kratki
     */
    float BOX_HEIGHT_F = 64.0f;
    // Ilosc kwadratów na planszy wg osi
    /**
     * Ilosc kwadratów na planszy wg osi X
     */
    int BOX_X_AXIS_NUMBER = 10;
    /**
     * Przelicznik kratek mapy tile do growej
     */
    float BoardBoxToTile = 0.5f;
    /**
     * Ilosc kwadratów na planszy wg osi Y
     */
    int BOX_Y_AXIS_NUMBER = 10;
    // Współrzędne wieżyczek na statkach
    /**
     * Pozycje wieżyczek na statku trój-polowym
     */
    Vector2f[] TurretsPos3 = { new Vector2f(8, 56), new Vector2f(31, 59), new Vector2f(52, 56),
            new Vector2f(8, 73), new Vector2f(31, 75), new Vector2f(52, 73), new Vector2f(16, 109),
            new Vector2f(31, 107), new Vector2f(47, 109), new Vector2f(31, 130) };
    /**
     * Pozycje wieżyczek na statku dwu-polowym
     */
    Vector2f[] TurretsPos2 = { new Vector2f(31, 21), new Vector2f(31, 37), new Vector2f(31, 73),
            new Vector2f(31, 92) };
    /**
     * Pozycje wieżyczek na statku jedno-polowym
     */
    Vector2f[] TurretsPos1 = { new Vector2f(25, 32), new Vector2f(36, 32) };

    // Opcje graficzne
    /**
     * Zmienna przechowująca czy aplikacja ma być w trybie pełnoekranowym
     */
    boolean FULLSCREENMODE = true;
    /**
     * Zmienna przechowująca włączenie Synchronizacji Pionowej
     */
    boolean VSYNCENABLED = false;
    /**
     * Zmienna przechowująca maksymlną ilość klatek
     */
    int FPSMAX = 999;
    /**
     * Zmienna przechowująca dodatkową opcję aplikacji
     */
    boolean UNDECORATED = false;
    /**
     * Zmienna przechowująca dodatkową opcję aplikacji
     */
    boolean ALLOWSOFTWAREMODE = true;

}
