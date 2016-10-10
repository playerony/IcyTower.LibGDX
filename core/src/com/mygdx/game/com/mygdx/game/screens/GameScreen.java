package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.controllers.*;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;
import com.mygdx.game.com.mygdx.game.entities.Background;
import com.mygdx.game.com.mygdx.game.entities.Floor;
import com.mygdx.game.com.mygdx.game.service.SoundService;

import java.io.FileNotFoundException;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class GameScreen extends AbstractScreen {
    private static final String PAUSE_INFORMATION_BUTTON_TEXT = "PAUSE";
    private static final String PAUSE_BUTTON_TEXT = "P";

    private static final float CAMERA_MOVEMENT_SPEED = 15;
    private float cameraVelocity = 0.0f;
    private float dieTime = 0;

    private boolean addOnStage = true;
    private boolean first = true;
    private boolean die = false;

    private BitmapFont font;
    private AnimatedImage anim;
    private Background background;
    private Texture logoTexture;

    private Label scoreLabel;
    private Label pauseLabel;
    private TextButton pauseButton;

    private Floor floor;
    private PlayerControler playerControler;
    private MenuScreen menuScreen;
    private CloudsControler cloudsControler;
    private ScoreControler scoreControler;
    private FlyingObjectControler flyingObjectControler;
    private PlatformAndEnemyControler platformAndEnemyControler;

    private Stage constantStage;

    public GameScreen(IcyTower game){
        super(game);

        init();

        logoTexture = game.assets.manager.get("assets/logo.png", Texture.class);
    }

    public void init() {
        initConstantStage();

        initClouds();
        initPlatforms();

        initBackground();
        initFloor();
        initFont();

        initPlayer();
        initMenuScreen();
        initPauseLabel();

        initScoreControler();
        initScoreLabel();

        initPauseButton();
        initFlyingObjectControler();
    }

    private void initConstantStage() {
        constantStage = new Stage(new StretchViewport(IcyTower.SCREEN_WIDTH, IcyTower.SCREEN_HEIGHT, cameraScore));
    }

    private void initClouds() {
        cloudsControler = new CloudsControler(stage, game);
    }

    private void initPlatforms() {
        platformAndEnemyControler = new PlatformAndEnemyControler(stage, game);
    }

    private void initBackground() {
        background = new Background(30, 200);
        stage.addActor(background);
    }

    private void initFloor() {
        floor = new Floor(0, -IcyTower.SCREEN_HEIGHT / 2 - 100 + 10);
        stage.addActor(floor);
    }

    private void initFont() {
        font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);
    }

    private void initPlayer() {
        playerControler = new PlayerControler(game, anim, camera);

        cameraScore.position.set(camera.position.x + 70, camera.position.y, 0);
        stage.addActor(playerControler.getAnim());
    }

    private void initMenuScreen() {
        menuScreen = new MenuScreen(stage, game.assets.manager.get("assets/logo.png", Texture.class));
    }

    private void initPauseLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        pauseLabel = new Label(PAUSE_INFORMATION_BUTTON_TEXT, style);
        pauseLabel.setPosition(camera.position.x, camera.position.y);
    }

    private void initScoreControler() {

        scoreControler = new ScoreControler();
    }

    private void initScoreLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        scoreLabel = new Label("MARIO\n" + menuScreen.getPoints(scoreControler.getSCORE()), style);
        scoreLabel.setPosition(-90, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 140);
    }

    private void initPauseButton() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);

        pauseButton = new TextButton(PAUSE_BUTTON_TEXT, style);
        pauseButton.setPosition(cameraScore.position.x + IcyTower.SCREEN_WIDTH / 3,
                cameraScore.position.y + IcyTower.SCREEN_HEIGHT / 3 + 40);

        pauseButton.addListener(new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setPause(!game.isPause());

                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void initFlyingObjectControler() {

        flyingObjectControler = new FlyingObjectControler(game.assets, stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderUpdate();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();
        batch.end();


        batch.begin();
        batch.setProjectionMatrix(cameraScore.combined);

        constantStage.draw();
        batch.end();
    }

    private void renderUpdate() {
        if ((first || !menuScreen.getMenuState()) && !game.isPause()) {
            first = false;
            update();
        }

        if (!menuScreen.getMenuState()) {
            scoreLabel.setText("MARIO\n" + menuScreen.getPoints(scoreControler.getSCORE()));

            if (addOnStage) {
                constantStage.addActor(pauseButton);
                constantStage.addActor(scoreLabel);
                flyingObjectControler.setRandomize(false);

                addOnStage = false;

                Gdx.input.setInputProcessor(constantStage);
            }

            if (game.isPause()) {
                constantStage.addActor(pauseLabel);
                flyingObjectControler.setRandomize(true);
            } else {
                pauseLabel.remove();
                flyingObjectControler.setRandomize(menuScreen.getMenuState());
            }
        } else {
            scoreLabel.remove();
            flyingObjectControler.setRandomize(true);

            addOnStage = true;
        }
    }

    private void update() {
        stage.act();

        backgroundUpdate();
        playerUpdate();
        cloudsControler.update(camera);
        platformAndEnemyControler.update(playerControler, scoreControler, anim, menuScreen);

        scoreControler.update();
        flyingObjectControler.update(playerControler.getPlayer());
    }

    private void backgroundUpdate() {
        background.update(playerControler.getPlayer().getX() / 300, playerControler.getPlayer().getWidth() / 2 + playerControler.getPlayer().getY() / 30);

        if (playerControler.getPlayer().getX() > IcyTower.SCREEN_WIDTH)
            playerControler.getPlayer().setX(-playerControler.getPlayer().getWidth());

        if (playerControler.getPlayer().getX() < -playerControler.getPlayer().getWidth())
            playerControler.getPlayer().setX(IcyTower.SCREEN_WIDTH);

        playerControler.getAnim().setPosition(playerControler.getPlayer().getX(), playerControler.getPlayer().getY());
        playerControler.getAnim().setAnimation(playerControler.getPlayer().getAnimation());

        if (!playerControler.getPlayer().getDie())
            camera.position.set(IcyTower.SCREEN_WIDTH / 2, playerControler.getPlayer().getY() + 200 + playerControler.getPlayer().getJumpVelocity() / 100 + cameraVelocity, 0);


        if (playerControler.getPlayer().getJumpVelocity() == 0)
            cameraVelocity += (CAMERA_MOVEMENT_SPEED * Gdx.graphics.getDeltaTime());

        else if (playerControler.getPlayer().getJumpVelocity() < 0)
            cameraVelocity += (CAMERA_MOVEMENT_SPEED * 3 * Gdx.graphics.getDeltaTime());

        else if (playerControler.getPlayer().getJumpVelocity() > 0 && cameraVelocity > 0)
            cameraVelocity -= (CAMERA_MOVEMENT_SPEED * 2 * Gdx.graphics.getDeltaTime());


        if (playerControler.getPlayer().getX() > IcyTower.SCREEN_WIDTH)
            playerControler.getPlayer().setX(-playerControler.getPlayer().getWidth());

        if (playerControler.getPlayer().getX() < -playerControler.getPlayer().getWidth())
            playerControler.getPlayer().setX(IcyTower.SCREEN_WIDTH);


        if (playerControler.getPlayer().getY() + IcyTower.SCREEN_HEIGHT > 5000) {
            floor.addAction(Actions.removeActor());
            floor.remove();

            background.addAction(Actions.removeActor());
            background.remove();
        }
    }

    private void playerUpdate() {
        playerControler.playerUpdate(scoreControler);
        playerDie();
    }

    private void playerDie() {
        if (playerControler.getPlayer().getDie())
            dieTime += Gdx.graphics.getDeltaTime();

        if (!playerControler.getPlayer().getDie() && playerControler.getPlayer().getY() + playerControler.getPlayer().getHeight() < camera.position.y - IcyTower.SCREEN_HEIGHT / 2) {
            playerControler.getPlayer().setDie(true);
            playerControler.getPlayer().setJump(false);
            playerControler.getPlayer().setJumpVelocity(700);

            game.getSoundService().pauseTitleSound();
            game.getSoundService().playDeathSound();
        } else if (dieTime > SoundService.DIE_SOUND_LENGTH) {
            die = true;
            dieTime = 0;
        }

        if (playerControler.getPlayer().getDie()) {
            if (die) {
                pauseButton.remove();

                playerControler.getPlayer().setDie(false);
                first = true;
                die = false;
                cameraVelocity = 0;

                try {
                    scoreControler.increaseScore(scoreControler.getScoreToAdd());
                    menuScreen.setLastScore(scoreControler.getSCORE());
                    scoreControler.setScoreToAdd(0);

                    menuScreen.saveNewRecord(scoreControler);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                scoreControler.setSCORE(0);

                platformAndEnemyControler.clearEnemiesAndPlatforms();
                flyingObjectControler.clearObjects();
                cloudsControler.clearClouds();

                playerControler.getPlayer().addAction(Actions.removeActor());
                //anim.addAction(Actions.removeActor());

                camera.position.set(IcyTower.SCREEN_WIDTH / 2, playerControler.getPlayer().getY() + 200 + playerControler.getPlayer().getJumpVelocity() / 100, 0);
                stage.clear();

                if (background != null)
                    initBackground();

                if (floor != null)
                    initFloor();

                initClouds();
                initPlatforms();
                initPlayer();
                initFlyingObjectControler();

                menuScreen.showMenu(stage);
                Gdx.input.setInputProcessor(stage);

                game.getSoundService().continueTitleSound();
            }
        }
    }
}
