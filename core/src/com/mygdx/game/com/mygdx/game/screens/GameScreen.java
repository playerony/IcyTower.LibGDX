package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.controllers.Enemy;
import com.mygdx.game.com.mygdx.game.controllers.FlyingObjectControler;
import com.mygdx.game.com.mygdx.game.controllers.ScoreControler;
import com.mygdx.game.com.mygdx.game.enemies.*;
import com.mygdx.game.com.mygdx.game.entities.*;
import com.mygdx.game.com.mygdx.game.service.SoundService;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class GameScreen extends AbstractScreen {
    private static final String PAUSE_INFORMATION_BUTTON_TEXT = "PAUSE";
    private static final String PAUSE_BUTTON_TEXT = "P";
    private static final int HEIGHT_BETWEEN_PLATFORMS = 200;
    private static final int SPECIAL_MAX_LENGTH_OF_PLATFORM = 10;

    private static final float CAMERA_MOVEMENT_SPEED = 1;
    private static final int PLATFORMS = 14;
    private static float enemyTimer = 0;
    private static int AMOUT_OF_ENEMIES = 1;
    private static int LEVEL_CLOUDS = 1;
    private static int LEVEL_PLATFORMS = 1;
    private static int LAST_NUMBER_OF_BLOCK = 0;
    private static int NUMBER_OF_BLOCK = 0;
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
    private Cloud cloud;
    private Player player;
    private MenuScreen menuScreen;
    private ScoreControler scoreControler;
    private FlyingObjectControler flyingObjectControler;

    private ArrayList<Platform> platformArray;
    private ArrayList<Enemy> enemyArray;
    private ArrayList<Enemy> enemyToRemoveArray;

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
        initEnemy();

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
        cloud = new Cloud(MathUtils.random(IcyTower.SCREEN_WIDTH),
                LEVEL_CLOUDS * IcyTower.SCREEN_HEIGHT + MathUtils.random(IcyTower.SCREEN_HEIGHT + 300) + 300,
                game.assets.manager.get("assets/cloud.png", Texture.class));

        LEVEL_CLOUDS++;

        stage.addActor(cloud);
    }

    private void initPlatforms() {
        platformArray = new ArrayList<>();

        for (int i = LEVEL_PLATFORMS; i < PLATFORMS; i++) {
            int length = MathUtils.random(3) + 3;

            Platform p = new Platform(MathUtils.random(IcyTower.SCREEN_WIDTH - length * 64),
                    LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS,
                    length, game.assets);

            LEVEL_PLATFORMS++;

            p.onStage(stage);
            platformArray.add(p);
        }
    }

    private void initEnemy() {
        enemyToRemoveArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
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
        platformUpdate();
        cloudsUpdate();

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


        if (player.getJumpVelocity() == 0)
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

                    saveNewRecord();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                scoreControler.setSCORE(0);

                AMOUT_OF_ENEMIES = 1;
                LEVEL_PLATFORMS = 1;
                LEVEL_CLOUDS = 1;

                for (Platform p : platformArray) {
                    p.removeBlocks();
                    p.addAction(Actions.removeActor());
                }

                for (Enemy e : enemyArray) {
                    e.removeEnemy();
                    e.remove();
                    e.addAction(Actions.removeActor());
                }

                flyingObjectControler.clearObjects();

                cloud.addAction(Actions.removeActor());
                cloud.remove();

                player.addAction(Actions.removeActor());
                anim.addAction(Actions.removeActor());

                platformArray.clear();
                cloud.clear();
                enemyArray.clear();

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

    private void platformUpdate() {
        for (Platform p : platformArray) {
            if (isPlayerOnPlatform(p) && !player.getDie()) {
                player.setJump(true);
                player.setJumpVelocity(0);
                player.setY(p.getY() + p.getHeight() - 4);
                player.setCollision(true);

                if (scoreControler.isAddPoints() && p.isPoints()) {
                    scoreControler.increaseScoreToAdd(AMOUT_OF_ENEMIES * LEVEL_PLATFORMS * 2);
                    scoreControler.setAddPoints(false);

                    p.setPoints(false);
                }
            }

            if (player.getY() - IcyTower.SCREEN_HEIGHT * 1.2f > p.getY()) {
                int length = 0;

                if (LEVEL_PLATFORMS % 10 != 0) {
                    length = MathUtils.random(2) + 3;

                    p.remove();

                    if (LEVEL_PLATFORMS >= 20)
                        p.setBricks(NUMBER_OF_BLOCK);

                    p.setSIZE(length);
                    p.setPos(MathUtils.random(IcyTower.SCREEN_WIDTH - (length * 64)), LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS);
                    p.onStage(stage);
                    p.setPoints(true);
                } else {
                    length = SPECIAL_MAX_LENGTH_OF_PLATFORM;

                    randomNewStage();
                    p.remove();
                    p.setBricks(NUMBER_OF_BLOCK);
                    p.setSIZE(length);
                    p.setPos(-100, LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS);
                    p.onStage(stage);
                    p.setPoints(true);
                }

                anim.toFront();

                LEVEL_PLATFORMS++;

                if (LEVEL_PLATFORMS % 2 == 0 && AMOUT_OF_ENEMIES <= 26)
                    AMOUT_OF_ENEMIES++;

                if (length != SPECIAL_MAX_LENGTH_OF_PLATFORM)
                    getRandomEnemy(p);

                else
                    addDragonOnStage(p);
            }

            enemyUpdate(p);
        }
    }

    private void addDragonOnStage(Platform p) {
        Enemy e = null;
        float START_POS_ON_PLATFOFRM = p.getX() + p.getWidth() / 2;

        switch (NUMBER_OF_BLOCK) {
            case 1:
                e = new Dragon(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/dragon_green.png", Texture.class));
                break;

            case 2:
                e = new Dragon(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/dragon_blue.png", Texture.class));
                break;

            case 3:
                e = new Dragon(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/dragon_grey.png", Texture.class));
                break;

            default:

                break;
        }

        if (e != null) {
            e.animation.setAnimation(e.getAnimation());
            e.animation.setPosition(e.getX(), e.getY());
            e.setSPEED(e.getSPEED() + (LEVEL_PLATFORMS / 12));

            stage.addActor(e.animation);
            enemyArray.add(e);
        }
    }

    private void randomNewStage() {
        do {
            LAST_NUMBER_OF_BLOCK = NUMBER_OF_BLOCK;
            NUMBER_OF_BLOCK = MathUtils.random(3);
        } while (LAST_NUMBER_OF_BLOCK == NUMBER_OF_BLOCK);
    }

    private void getRandomEnemy(Platform p) {
        int value = MathUtils.random(6);

        if (value <= 3 && enemyArray.size() <= 5 + (LEVEL_PLATFORMS / 10)) {
            Enemy e = null;
            int type = MathUtils.random(AMOUT_OF_ENEMIES);
            float START_POS_ON_PLATFOFRM = p.getX() + p.getWidth() / 2;

            switch (type) {
                case 0:
                    e = new Worm(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_1_red.png", Texture.class));
                    break;

                case 2:
                    e = new Turtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_2_green.png", Texture.class));
                    break;

                case 4:
                    e = new Worm(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_1_blue.png", Texture.class));
                    break;

                case 6:
                    e = new Turtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_3_black.png", Texture.class));
                    break;

                case 8:
                    e = new Worm(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_pink.png", Texture.class));
                    break;

                case 10:
                    e = new Turtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_2_red.png", Texture.class));
                    break;

                case 12:
                    e = new BigTurtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/bigturtle_green.png", Texture.class));
                    break;

                case 14:
                    e = new Hedgehog(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/hedgehog.png", Texture.class));
                    break;

                case 16:
                    e = new Turtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_3_blue.png", Texture.class));
                    break;

                case 18:
                    e = new Worm(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_black.png", Texture.class));
                    break;

                case 20:
                    e = new Worm(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_1_grey.png", Texture.class));
                    break;

                case 22:
                    e = new BigTurtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/bigturtle_grey.png", Texture.class));
                    break;

                case 24:
                    e = new Worm(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_blue.png", Texture.class));
                    break;

                case 26:
                    e = new Turtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_2_blue.png", Texture.class));
                    break;

                case 28:
                    e = new Turtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_3_grey.png", Texture.class));
                    break;

                case 30:
                    e = new Worm(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_grey.png", Texture.class));
                    break;

                case 32:
                    e = new BigTurtle(START_POS_ON_PLATFOFRM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/bigturtle_blue.png", Texture.class));
                    break;

                default:

                    break;
            }

            if (e != null) {
                e.animation.setAnimation(e.getAnimation());
                e.animation.setPosition(e.getX(), e.getY());
                e.setSPEED(e.getSPEED() + (LEVEL_PLATFORMS / 12));

                stage.addActor(e.animation);
                enemyArray.add(e);
            }
        }
    }

    private void enemyUpdate(Platform p) {
        Enemy mob = null;

        for (Enemy e : enemyArray) {
            if (isEnemyOnPlatform(e, p) && (e.getX() + (e.getSPEED() * Gdx.graphics.getDeltaTime()) >= p.getX() + p.getBounds().getWidth() - e.getWidth()
                    || e.getX() - (e.getSPEED() * Gdx.graphics.getDeltaTime()) <= p.getX())) {
                e.oppositeSPEED();

                if (e.getX() - (e.getSPEED() * Gdx.graphics.getDeltaTime()) <= p.getX())
                    e.setX(p.getX() + e.getSPEED() * Gdx.graphics.getDeltaTime());

                else if ((e.getX() + (e.getSPEED() * Gdx.graphics.getDeltaTime()) >= p.getX() + p.getBounds().getWidth() - e.getWidth()))
                    e.setX(p.getX() + p.getBounds().getWidth() - e.getWidth() - (e.getSPEED() * Gdx.graphics.getDeltaTime()));
            }

            e.update();

            if (!player.getDie() && e.getMove()) {
                if (player.getBottomBound().overlaps(e.getTopBound())) {
                    e.die();

                    if (!e.getDragonInfo()) {
                        game.getSoundService().playStompSound();
                        scoreControler.increaseScoreToAdd(LEVEL_PLATFORMS * 100);
                        player.setJumpVelocity(1475);
                    } else {
                        game.getSoundService().playSuperJumpSound();
                        scoreControler.increaseScoreToAdd(LEVEL_PLATFORMS * 300);
                        player.setJumpVelocity(1675);
                    }

                    player.setRotate(true);
                    player.setFlip(true);

                    enemyToRemoveArray.add(e);
                }

                if (player.getRightBound().overlaps(e.getLeftBound()) ||
                        player.getLeftBound().overlaps(e.getRightBound()) ||
                        player.getTopBound().overlaps(e.getBottomBound())) {

                    player.setDie(true);
                    player.setJumpVelocity(600);

                    game.getSoundService().pauseTitleSound();
                    game.getSoundService().playDeathSound();

                    try {
                        saveNewRecord();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (player.getY() - IcyTower.SCREEN_HEIGHT * 1.2f > e.getY())
                mob = e;
        }

        if (mob != null) {
            mob.removeEnemy();
            mob.remove();
            enemyArray.remove(mob);
        }

        enemyDie();
    }

    private void enemyDie() {
        if (enemyToRemoveArray != null) {
            Enemy ene = null;
            enemyTimer += Gdx.graphics.getDeltaTime();

            if (enemyTimer > 1.0f) {
                for (Enemy e : enemyToRemoveArray) {
                    ene = e;
                    break;
                }

                if (ene != null) {
                    ene.animation.addAction(Actions.removeActor());
                    enemyArray.remove(ene);
                    ene.remove();
                }

                enemyTimer = 0;
            }
        }
    }

    private void saveNewRecord() throws FileNotFoundException {
        if (scoreControler.getSCORE() > menuScreen.getBestScore()) {
            PrintWriter zapis = new PrintWriter("assets/bestScore.txt");
            zapis.println(scoreControler.getSCORE());

            zapis.close();
        }
    }

    private void cloudsUpdate() {
        cloud.update();

        if (cloud.getY() < camera.position.y - IcyTower.SCREEN_HEIGHT) {
            cloud.setPosition(-cloud.getWidth(), MathUtils.random(camera.position.y, camera.position.y + IcyTower.SCREEN_HEIGHT));
            cloud.setNewSpeed();

            LEVEL_CLOUDS++;
        }

        if (cloud.getX() >= IcyTower.SCREEN_WIDTH)
            cloud.setX(-cloud.getWidth());

        else if (cloud.getX() <= -cloud.getWidth())
            cloud.setX(IcyTower.SCREEN_WIDTH);
    }

    private boolean isEnemyOnPlatform(Enemy e, Platform p) {
        return e.getBottomBound().overlaps(p.getBounds());
    }

    private boolean isPlayerOnPlatform(Platform p) {
        return player.getJumpVelocity() < 0 && player.getBottomBound().overlaps(p.getBounds()) && !(player.getY() < p.getY());
    }
}
