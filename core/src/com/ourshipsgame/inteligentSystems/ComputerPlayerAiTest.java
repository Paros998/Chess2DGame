package com.ourshipsgame.inteligentSystems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.math.Vector2;

import org.junit.jupiter.api.Test;

/**
 * Test Junit
 */
public class ComputerPlayerAiTest {
    @Test
    public void test() {
        int[][] ShotsDone = new int[10][10];
        int i, j;
        i = j = 0;
        for (i = 0; i < 10; i++)
            for (j = 0; j < 10; j++)
                ShotsDone[i][j] = 0;
        // setting enviroment
        ComputerPlayerAi aiTestAi = new ComputerPlayerAi(ShotsDone);
        ShotsDone[5][5] = 1;// hitted
        ShotsDone[4][5] = -1;// missed
        ShotsDone[6][5] = -1;// missed
        ShotsDone[5][6] = -1;// missed
        aiTestAi.attackEnemy(1.1f);
        assertEquals(new Vector2(5, 4), new Vector2(aiTestAi.getX(), aiTestAi.getY()),
                "TEST FAILED - Not generated the best position to shoot");

    }
}
