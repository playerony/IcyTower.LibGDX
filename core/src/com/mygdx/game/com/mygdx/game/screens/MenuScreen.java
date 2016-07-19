package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.IcyTower;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by pawel_000 on 2016-06-15.
 */
public class MenuScreen {

    private static final String START_BUTTON_TEXT = "I PLAYER GAME";

    private TextButton startGameButton;
    private Label lastScoreLabel;
    private Label scoreLabel;
    private Image logoImage;

    private int LAST_SCORE = 0;
    private int BEST_SCORE = 0;
    private boolean menu = true;

    public MenuScreen(Stage stage, final Texture logoTexture) {
        logoImage = new Image(logoTexture);

        init(stage);
    }

    private void init(Stage stage) {
        initStartGameButton();
        initLastScoreLabel();
        initScoreLabel();
        initLogo();

        showMenu(stage);
    }

    public void showMenu(Stage stage) {
        menu = true;

        BEST_SCORE = getScoreFromFile();
        scoreLabel.setText("TOP- " + getPoints(BEST_SCORE));

        stage.addActor(startGameButton);
        stage.addActor(scoreLabel);
        stage.addActor(logoImage);
        stage.addActor(lastScoreLabel);
    }

    private void initLogo() {
        logoImage.setPosition(IcyTower.SCREEN_WIDTH / 2 - logoImage.getWidth() / 2,
                IcyTower.SCREEN_WIDTH - logoImage.getHeight() + 45);
    }

    private void initStartGameButton() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        startGameButton = new TextButton(START_BUTTON_TEXT, style);
        startGameButton.setPosition(IcyTower.SCREEN_WIDTH / 2 - startGameButton.getWidth() / 2,
                IcyTower.SCREEN_HEIGHT - 480);

        startGameButton.addListener(new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menu = false;

                startGameButton.addAction(Actions.removeActor());
                lastScoreLabel.addAction(Actions.removeActor());
                scoreLabel.addAction(Actions.removeActor());
                logoImage.addAction(Actions.removeActor());

                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void initLastScoreLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        lastScoreLabel = new Label("LAST- " + getPoints(LAST_SCORE), style);
        lastScoreLabel.setPosition(IcyTower.SCREEN_WIDTH / 2 - lastScoreLabel.getWidth() / 2, startGameButton.getY() - startGameButton.getMinWidth() / 2 + 30);
    }

    private void initScoreLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        scoreLabel = new Label("TOP- " + getPoints(getScoreFromFile()), style);
        scoreLabel.setPosition(IcyTower.SCREEN_WIDTH / 2 - scoreLabel.getWidth() / 2, startGameButton.getY() - startGameButton.getMinWidth() / 2 - 20);
    }

    private int getScoreFromFile() {
        File file = new File("assets/bestScore.txt");
        Scanner in = null;

        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return in.nextInt();
    }

    public String getPoints(int value) {
        String result = "000000";

        if (value == 0)
            result = "000000";
        else if (value < 9 && value > 0)
            result = "00000" + Integer.toString(value);
        else if (value < 99 && value >= 10)
            result = "0000" + Integer.toString(value);
        else if (value < 999 && value >= 100)
            result = "000" + Integer.toString(value);
        else if (value < 9999 && value >= 1000)
            result = "00" + Integer.toString(value);
        else if (value < 99999 && value >= 10000)
            result = "0" + Integer.toString(value);
        else
            result = Integer.toString(value);

        return result;
    }

    //////////// GETTERS

    public boolean getMenuState() {
        return this.menu;
    }

    public int getBestScore() {
        return BEST_SCORE;
    }

    //////////// SETTERS

    public void setBestScore(int BEST_SCORE) {
        this.BEST_SCORE = BEST_SCORE;
    }

    public void setLastScore(int score) {
        this.LAST_SCORE = score;

        lastScoreLabel.setText("LAST- " + getPoints(score));
    }
}
