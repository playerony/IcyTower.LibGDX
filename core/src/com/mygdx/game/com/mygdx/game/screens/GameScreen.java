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
import com.mygdx.game.com.mygdx.game.controllers.CloudsControler;
import com.mygdx.game.com.mygdx.game.controllers.FlyingObjectControler;
import com.mygdx.game.com.mygdx.game.controllers.PlatformAndEnemyControler;
import com.mygdx.game.com.mygdx.game.controllers.ScoreControler;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;
import com.mygdx.game.com.mygdx.game.entities.Background;
import com.mygdx.game.com.mygdx.game.entities.Floor;
import com.mygdx.game.com.mygdx.game.entities.Player;
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
    private Player player;
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
        player = new Player(game.assets.manager.get("assets/mario.png", Texture.class), game);

        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + 250, 0);
        cameraScore.position.set(camera.position.x + 70, camera.position.y, 0);

        anim = new AnimatedImage(player.getAnimation());
        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());
        anim.setOrigin(player.getWidth() / 2, player.getHeight() / 2);

        stage.addActor(anim);
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
        platformAndEnemyControler.update(player, scoreControler, anim, menuScreen);

        scoreControler.update();
        flyingObjectControler.update(player);
    }

    private void backgroundUpdate() {
        background.update(player.getX() / 300, player.getWidth() / 2 + player.getY() / 30);

        if(player.getX() > IcyTower.SCREEN_WIDTH)
            player.setX( -player.getWidth() );

        if(player.getX() < -player.getWidth())
            player.setX( IcyTower.SCREEN_WIDTH );

        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());

        if (!player.getDie())
            camera.position.set(IcyTower.SCREEN_WIDTH / 2, player.getY() + 200 + player.getJumpVelocity() / 100 + cameraVelocity, 0);


        if (player.getJumpVelocity() <= 0)
            cameraVelocity += (CAMERA_MOVEMENT_SPEED * Gdx.graphics.getDeltaTime());

        else if (player.getJumpVelocity() > 0 && cameraVelocity > 0)
            cameraVelocity -= (CAMERA_MOVEMENT_SPEED * 2 * Gdx.graphics.getDeltaTime());


        if (player.getX() > IcyTower.SCREEN_WIDTH)
            player.setX(-player.getWidth());

        if (player.getX() < -player.getWidth())
            player.setX(IcyTower.SCREEN_WIDTH);


        if (player.getY() + IcyTower.SCREEN_HEIGHT > 5000) {
            floor.addAction(Actions.removeActor());
            floor.remove();

            background.addAction(Actions.removeActor());
            background.remove();
        }
    }

    private void playerUpdate() {
        player.update();
        playerDie();
        playerRotation();

        if (player.getJumpVelocity() < -1200 && !player.getDie()) {
            player.setDie(true);
            player.setJump(false);
            player.setJumpVelocity(800);

            game.getSoundService().pauseTitleSound();
            game.getSoundService().playDeathSound();
        }

        if (player.getJumpVelocity() < 0) {
            scoreControler.setAddPoints(true);
        }
    }

    private void playerRotation() {
        if (player.getRotate()) {
            if (player.getAngle() >= 360) {
                player.setAngle(0.0f);
                player.setRotate(false);
                player.setFlip(false);
            }

            anim.setRotation(player.getAngle());
            player.setAngle(player.getAngle() + 10.25f);
        }
    }

    private void playerDie() {
        if (player.getDie())
            dieTime += Gdx.graphics.getDeltaTime();

        if (!player.getDie() && player.getY() + player.getHeight() < camera.position.y - IcyTower.SCREEN_HEIGHT / 2) {
            player.setDie(true);
            player.setJump(false);
            player.setJumpVelocity(700);

            game.getSoundService().pauseTitleSound();
            game.getSoundService().playDeathSound();
        } else if (dieTime > SoundService.DIE_SOUND_LENGTH) {
            die = true;
            dieTime = 0;
        }

        if (player.getDie()) {
            if (die) {
                pauseButton.remove();

                player.setDie(false);
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

                player.addAction(Actions.removeActor());
                anim.addAction(Actions.removeActor());

                camera.position.set(IcyTower.SCREEN_WIDTH / 2, player.getY() + 200 + player.getJumpVelocity() / 100, 0);
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
