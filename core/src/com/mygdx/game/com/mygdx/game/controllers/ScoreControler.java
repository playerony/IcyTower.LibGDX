package com.mygdx.game.com.mygdx.game.controllers;

/**
 * Created by pawel_000 on 2016-07-09.
 */
public class ScoreControler {
    private static int SCORE = 0;
    private static int LAST_SCORE = 0;
    private static int SCORE_TO_ADD = 0;

    private boolean addPoints = true;

    public ScoreControler() {
        init();
    }

    private void init() {

    }

    public void update() {
        addScore();
    }

    private void addScore() {
        if (SCORE_TO_ADD > 0) {
            if (SCORE_TO_ADD < 100) {
                SCORE += 1;
                SCORE_TO_ADD -= 1;

            } else if (SCORE_TO_ADD <= 1000 && SCORE_TO_ADD >= 100) {
                SCORE += 10;
                SCORE_TO_ADD -= 10;

            } else if (SCORE_TO_ADD > 1000) {
                SCORE += 100;
                SCORE_TO_ADD -= 100;
            }
        }
    }

    public void increaseScoreToAdd(int value) {
        SCORE_TO_ADD += value;
    }

    public void increaseScore(int value) {
        SCORE += value;
    }

    // GETTETS
    public int getSCORE() {
        return SCORE;
    }

    // SETTERS
    public void setSCORE(int value) {
        SCORE = value;
    }

    public int getLastScore() {
        return LAST_SCORE;
    }

    public void setLastScore(int value) {
        LAST_SCORE = value;
    }

    public int getScoreToAdd() {
        return SCORE_TO_ADD;
    }

    public void setScoreToAdd(int value) {
        SCORE_TO_ADD = value;
    }

    public boolean isAddPoints() {
        return addPoints;
    }

    public void setAddPoints(boolean addPoints) {
        this.addPoints = addPoints;
    }
}
