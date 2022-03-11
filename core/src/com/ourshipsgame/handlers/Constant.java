package com.ourshipsgame.handlers;

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
    int BOX_WIDTH = 120;
    /**
     * Wysokość jednej kratki
     */
    int BOX_HEIGHT = 120;
    /**
     * Szerokość jednej kratki
     */
    float BOX_WIDTH_F = 120.0f;
    /**
     * Wysokość jednej kratki
     */
    float BOX_HEIGHT_F = 120.0f;
    // Ilosc kwadratów na planszy wg osi
    /**
     * Ilosc kwadratów na planszy wg osi X
     */
    int BOX_X_AXIS_NUMBER = 8;
    /**
     * Przelicznik kratek mapy tile do growej
     */
    float BoardBoxToTile = 0.5f;
    /**
     * Ilosc kwadratów na planszy wg osi Y
     */
    int BOX_Y_AXIS_NUMBER = 8;

    float X_AXIS_BOARD_START = 1920.f - 1080.f / 2;

    float Y_AXIS_BOARD_START = 0.f;

    //Mają po 120px nie 128
    String[] ChessPiecesTexturesPaths = {
            "core/assets/chess-pieces/b_bishop_png_shadow_128px.png",
            "core/assets/chess-pieces/b_king_png_shadow_128px.png",
            "core/assets/chess-pieces/b_knight_png_shadow_128px.png",
            "core/assets/chess-pieces/b_pawn_png_shadow_128px.png",
            "core/assets/chess-pieces/b_queen_png_shadow_128px.png",
            "core/assets/chess-pieces/b_rook_png_shadow_128px.png",

            "core/assets/chess-pieces/w_bishop_png_shadow_128px.png",
            "core/assets/chess-pieces/w_king_png_shadow_128px.png",
            "core/assets/chess-pieces/w_knight_png_shadow_128px.png",
            "core/assets/chess-pieces/w_pawn_png_shadow_128px.png",
            "core/assets/chess-pieces/w_queen_png_shadow_128px.png",
            "core/assets/chess-pieces/w_rook_png_shadow_128px.png",
    };

    enum ChessPiecesPaths {
        B_BISHOP,
        B_KING,
        B_KNIGHT,
        B_PAWN,
        B_QUEEN,
        B_ROOK,
        W_BISHOP,
        W_KING,
        W_KNIGHT,
        W_PAWN,
        W_QUEEN,
        W_ROOK
    }

    enum ChessPiecesInArray {
        King,
        Queen,
        FstBishop,
        SndBishop,
        FstKnight,
        SndKnight,
        FstRook,
        SndRook,
        Pawn1,
        Pawn2,
        Pawn3,
        Pawn4,
        Pawn5,
        Pawn6,
        Pawn7,
        Pawn8
    }


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
