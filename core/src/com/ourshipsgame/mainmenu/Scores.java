package com.ourshipsgame.mainmenu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Klasa ,której rolą jest tworzenie pliku scores.txt oraz przechowywanie
 * wszystkich wyników.
 */
public class Scores {
    /**
     * Klasa zagnieżdzona przechowująca dane pojedyńczego rekordu wyniku.
     */
    protected class Node {
        /**
         * Identyfikator rekordu
         */
        protected int id;
        /**
         * Zmienna przechowująca nazwę gracza
         */
        protected String name;
        /**
         * Zmienna przechowująca wynik
         */
        protected float scoreValue;
        /**
         * Zmienna przechowująca czas trwania tur gracza
         */
        protected float timeElapsed;
        /**
         * Zmienna przechowująca współczynnik procentowy trafień do wystrzałów
         */
        protected float accuracyRatio;

        /**
         * Konstruktor obiektu rekordu
         * 
         * @param name     Zmienna przechowująca nazwę gracza
         * @param score    Zmienna przechowująca wynik
         * @param time     Zmienna przechowująca czas trwania tur gracza
         * @param accuracy Zmienna przechowująca współczynnik procentowy trafień do
         *                 wystrzałów Kontruktor klasy Node, który tworzy kolejny rekord
         *                 wyniku na podstawie danych pobranych z pliku
         */
        protected Node(String name, float score, float time, float accuracy) {
            this.name = name;
            this.scoreValue = score;
            this.timeElapsed = time;
            this.accuracyRatio = accuracy;
        }
    }

    /**
     * Klasa dodatkowa pozwalająca przeciążyć komparator do listy typu ArrayList
     */
    protected class SortByScore implements Comparator<Node> {
        /**
         * Przeciążona metoda z Comparatora ,która pozwala sortować klasie Scores
         * rekordy po wynikach
         */
        @Override
        public int compare(Node o1, Node o2) {
            return (int) (o2.scoreValue - o1.scoreValue);
        }

    }

    /**
     * Lista przechowująca rekordy
     */
    List<Node> scoresList;

    /**
     * Konstruktor klasy Scores tworzący listę typu ArrayList i wypełniający ją
     * rekordami
     */
    public Scores() {
        this.scoresList = new ArrayList<Node>();
        try {
            loadScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws IOException Metoda ta tworzy plik z wynikami jeśli nie istnieje lub
     *                     pobiera dane z pliku i umieszcza je w rekordach listy a
     *                     następnie sortuje listę
     */
    private void loadScores() throws IOException {
        File file = new File("scores.txt");
        if (!file.exists()) {
            file.createNewFile();
        } else {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String name = scanner.next().trim();
                float score = Float.valueOf(scanner.next().trim());
                float time = Float.valueOf(scanner.next().trim());
                float accuracy = Float.valueOf(scanner.next().trim());
                scoresList.add(new Node(name, score, time, accuracy));
            }
            scanner.close();
            scoresList.sort(new SortByScore());
        }
    }

    /**
     * Metoda rysująca na ekranie rekordy
     * 
     * @param batch SpriteBatch wykorzystywany do rysowania na ekranie
     * @param font  Czcionka używana do wyświetlania tesktu
     * @param gameH Zmienna określająca wysokość okna w pikselach
     * @param gameW Zmienna określająca szerokość okna w pikselach Metoda ta ma za
     *              zadanie wypisać wszystkie rekordy z listy na ekranie
     */
    public void drawScores(SpriteBatch batch, BitmapFont font, float gameH, float gameW) {
        Node node;
        for (int i = 0; i < scoresList.size(); i++) {
            node = scoresList.get(i);
            String text = i + " - " + node.name + " Score: " + node.scoreValue + " Time: "
                    + String.format("%.2f", node.timeElapsed) + " Shots accuracy: " + node.accuracyRatio;
            font.draw(batch, text, gameW / 2 - 300, gameH - 50 - (i * 50));
        }
    }
}
