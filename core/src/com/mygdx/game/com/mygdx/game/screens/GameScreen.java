package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.AssetsManager.Asset;
import com.mygdx.game.com.mygdx.game.enemies.Enemy;
import com.mygdx.game.com.mygdx.game.enemies.Turtle;
import com.mygdx.game.com.mygdx.game.enemies.Worm;
import com.mygdx.game.com.mygdx.game.entities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class GameScreen extends AbstractScreen {

    private static final String START_BUTTON_TEXT = "I PLAYER GAME";
    private static final int HEIGHT_BETWEEN_PLATFORMS = 200;
    private static final int PLATFORMS = 12;
    private static int LEVEL_CLOUDS = 1;
    private static int LEVEL_PLATFORMS = 1;
    private static int SCORE = 0;
    private static int LAST_SCORE = 0;
    private static int add = 0;
    private static int value = 0;
    private static float dieTimer = 0;
    private static float enemyTimer = 0;
    TextButton startGameButton;
    private Asset assets;
    private Texture logoTexture;
    private int CLOUDS = 10;
    private BitmapFont font;
    private String BEST = "";
    private boolean addPoints = true;
    private boolean menu = true;
    private boolean first = true;
    private Player player;
    private AnimatedImage anim;
    private Floor floor;
    private Background background;
    private ArrayList<Platform> platformArray;
    private ArrayList<Cloud> cloudArray;
    private ArrayList<Enemy> enemyArray;
    private ArrayList<Enemy> removeEnemyArray;

    public GameScreen(IcyTower game){
        super(game);

        assets = new Asset();
        assets.load();
        assets.manager.finishLoading();

        if (assets.manager.update())
            init();

        logoTexture = assets.manager.get("assets/logo.png", Texture.class);
    }

    private void init(){

        initClouds();
        initPlatforms();
        initFont();
        initEnemy();

        try {
            initBest();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        initBackground();
        initFloor();

        initPlayer();
    }

    private void initBest() throws FileNotFoundException {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;

        startGameButton = new TextButton(START_BUTTON_TEXT, style);

        File file = new File("assets/bestScore.txt");
        Scanner in = new Scanner(file);

        BEST = in.nextLine();
    }

    private void initFloor() {
        floor = new Floor(0, -IcyTower.SCREEN_HEIGHT / 2 - 100 + 10);
        stage.addActor(floor);
    }

    private void initBackground() {
        background = new Background(30, 250);
        stage.addActor(background);
    }

    private void initEnemy() {
        removeEnemyArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
    }

    private void initFont() {
        font = new BitmapFont(Gdx.files.internal("assets/font.fnt"), Gdx.files.internal("assets/font.png"), false);
    }

    private void initClouds() {
        cloudArray = new ArrayList<>();

        for(int i=1 ; i<CLOUDS ; i++) {
            Cloud c;
            c = new Cloud(MathUtils.random(IcyTower.SCREEN_WIDTH),
                    LEVEL_CLOUDS * IcyTower.SCREEN_HEIGHT + MathUtils.random(IcyTower.SCREEN_HEIGHT + 300) + 300,
                    assets.manager.get("assets/cloud.png", Texture.class));

            LEVEL_CLOUDS++;

            stage.addActor(c);
            cloudArray.add(c);
        }
    }

    private void initPlayer() {
        player = new Player(assets.manager.get("assets/mario.png", Texture.class));
        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + 250, 0);
        cameraScore.position.set(camera.position.x + 70, camera.position.y, 0);

        anim = new AnimatedImage(player.getAnimation());

        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());

        stage.addActor(anim);
    }

    private void initPlatforms() {
        platformArray = new ArrayList<>();

        for (int i = LEVEL_PLATFORMS; i < PLATFORMS; i++) {
            int length = MathUtils.random(3) + 3;

            Platform p = new Platform(MathUtils.random(IcyTower.SCREEN_WIDTH - length * 64), LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS, length);
            p.onStage(stage);

            LEVEL_PLATFORMS++;

            platformArray.add(p);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (first || !menu) {
            first = false;
            update();
        }

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();
        batch.end();

        batch.begin();
        batch.setProjectionMatrix(cameraScore.combined);

        if (!menu) {
            if (SCORE == 0)
                font.draw(batch, "MARIO\n" + "000000", 0, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 100);
            else if (SCORE < 9 && SCORE > 0)
                font.draw(batch, "MARIO\n" + "00000" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 100);
            else if (SCORE < 99 && SCORE >= 10)
                font.draw(batch, "MARIO\n" + "0000" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 100);
            else if (SCORE < 999 && SCORE >= 100)
                font.draw(batch, "MARIO\n" + "000" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 100);
            else if (SCORE < 9999 && SCORE >= 1000)
                font.draw(batch, "MARIO\n" + "00" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 100);
            else if (SCORE < 99999 && SCORE >= 10000)
                font.draw(batch, "MARIO\n" + "0" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 100);
            else
                font.draw(batch, "MARIO\n" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight() - 100);
        } else {
            font.draw(batch, "TOP- " + BEST, 70, font.getXHeight() + 70);

            if (LAST_SCORE > 0)
                font.draw(batch, "LAST- " + getPoints(LAST_SCORE), 70, font.getXHeight() + 120);

            batch.draw(logoTexture, IcyTower.SCREEN_WIDTH / 2 - logoTexture.getWidth() / 2 - 35, IcyTower.SCREEN_HEIGHT - logoTexture.getHeight() - 200);

            startGameButton.setPosition(82, IcyTower.SCREEN_HEIGHT - logoTexture.getHeight() - 300);

            stage.addActor(startGameButton);

            startGameButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    menu = false;

                    startGameButton.addAction(Actions.removeActor());

                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }

        batch.end();
    }

    private void update() {
        stage.act();

        player.update();

        background.update(player.getX() / 300, player.getWidth() / 2 + player.getY() / 30);

        if(player.getX() > IcyTower.SCREEN_WIDTH)
            player.setX( -player.getWidth() );

        if(player.getX() < -player.getWidth())
            player.setX( IcyTower.SCREEN_WIDTH );

        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());

        if (!player.getDie())
            camera.position.set(IcyTower.SCREEN_WIDTH / 2, player.getY() + 200 + player.getJumpVelocity() / 100, 0);

        for(Platform p : platformArray) {
            if (isPlayerOnPlatform(p) && !player.getDie()) {
                player.setJump(true);
                player.setJumpVelocity(0);
                player.setY(p.getY() + p.getHeight() - 4);
                player.setCollision(true);

                if (addPoints && p.isPoints()) {
                    add += (2 * LEVEL_PLATFORMS);
                    addPoints = false;

                    p.setPoints(false);
                }
            }

            if(player.getY() - IcyTower.SCREEN_HEIGHT * 2 > p.getY()){
                int length = MathUtils.random(3) + 3;

                p.setSIZE(length);
                p.setPoints(true);
                p.setPos(stage, MathUtils.random(IcyTower.SCREEN_WIDTH - (length * 64)), LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS);

                LEVEL_PLATFORMS++;

                int value = MathUtils.random(6);

                if(value < 3){
                    Enemy e = null;
                    int type = MathUtils.random(1);

                    switch (type) {
                        case 0:
                            e = new Worm(p.getX() + 15, p.getY() + p.getHeight() - 4);
                            break;

                        default:
                        case 1:
                            e = new Turtle(p.getX() + 15, p.getY() + p.getHeight() - 4);
                            break;
                    }

                    if (e != null) {
                        e.enem.setAnimation(e.getAnimation());
                        e.enem.setPosition(e.getX(), e.getY());

                        stage.addActor(e.enem);
                        enemyArray.add(e);
                    }
                }
            }

            enemyUpdate(p);
        }

        cloudsUpdate();

        if(player.getJumpVelocity() > 0)
            addPoints = true;

        if(add > 0){
            if(add < 100) {
                SCORE += 1;
                add -= 1;
            } else if(add >= 100){
                SCORE += 10;
                add -= 10;
            }

        } else
            value = 0;

        if(removeEnemyArray.size() > 0){
            enemyTimer += Gdx.graphics.getDeltaTime();

            if (enemyTimer > 1.0f) {
                for (Enemy e : removeEnemyArray) {
                    e.enem.addAction(Actions.removeActor());

                    enemyArray.remove(e);
                }

                removeEnemyArray.clear();
                enemyTimer = 0;
            }
        }

        if (player.getDie()) {
            dieTimer += Gdx.graphics.getDeltaTime();

            if (dieTimer > 1.1f) {
                player.setDie(false);
                menu = true;
                first = true;
                LAST_SCORE = SCORE;
                SCORE = 0;
                player.setPosition(0, 100);
                anim.setPosition(0, 100);
                anim.setAnimation(player.getAnimation());
                dieTimer = 0;

                LEVEL_PLATFORMS = 1;
                LEVEL_CLOUDS = 1;

                for (Platform p : platformArray) {
                    p.addAction(Actions.removeActor());
                }

                for (Cloud c : cloudArray) {
                    c.addAction(Actions.removeActor());
                }

                for (Enemy e : enemyArray) {
                    e.enem.addAction(Actions.removeActor());
                    e.addAction(Actions.removeActor());
                }

                platformArray.clear();
                cloudArray.clear();
                enemyArray.clear();

                camera.position.set(IcyTower.SCREEN_WIDTH / 2, player.getY() + 200 + player.getJumpVelocity() / 100, 0);

                initClouds();
                initPlatforms();
            }
        }
    }

    private void enemyUpdate(Platform p) {
        for(Enemy e : enemyArray) {

            if (isEnemyOnPlatform(e, p) && (e.getX() > p.getX() + p.getBounds().getWidth() - e.getBounds().getWidth() || e.getX() < p.getX())) {
                e.oppositeSPEED();
            }

            if (!player.getDie() && e.getMove()) {
                if (player.getBottomBound().overlaps(e.getTopBound())) {
                    e.setMove(false);
                    removeEnemyArray.add(e);
                }

                if (player.getRightBound().overlaps(e.getLeftBound()) ||
                        player.getLeftBound().overlaps(e.getRightBound()) ||
                        player.getTopBound().overlaps(e.getBottomBound())) {

                    player.setDie(true);
                    player.setJumpVelocity(500);

                    try {
                        saveNewRecord();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            e.update();
        }
    }

    private void saveNewRecord() throws FileNotFoundException {
        if (SCORE > Integer.parseInt(BEST)) {
            PrintWriter zapis = new PrintWriter("assets/bestScore.txt");

            if (SCORE == 0)
                zapis.println("000000");
            else if (SCORE < 9 && SCORE > 0)
                zapis.println("00000" + Integer.toString(SCORE));
            else if (SCORE < 99 && SCORE >= 10)
                zapis.println("0000" + Integer.toString(SCORE));
            else if (SCORE < 999 && SCORE >= 100)
                zapis.println("000" + Integer.toString(SCORE));
            else if (SCORE < 9999 && SCORE >= 1000)
                zapis.println("00" + Integer.toString(SCORE));
            else if (SCORE < 99999 && SCORE >= 10000)
                zapis.println("0" + Integer.toString(SCORE));
            else
                zapis.println(Integer.toString(SCORE));

            zapis.close();
        }
    }

    private void cloudsUpdate() {
        for(Cloud c : cloudArray){
            c.update();

            if(player.getY() - IcyTower.SCREEN_HEIGHT > c.getY()){
                c.setPosition(MathUtils.random(IcyTower.SCREEN_WIDTH), LEVEL_CLOUDS * IcyTower.SCREEN_HEIGHT + MathUtils.random(IcyTower.SCREEN_HEIGHT + 300) + 300);
                c.setNewSpeed();

                LEVEL_CLOUDS++;
            }

            if(c.getX() >= IcyTower.SCREEN_WIDTH)
                c.setX( -c.getWidth() );

            else if(c.getX() <= -c.getWidth())
                c.setX( IcyTower.SCREEN_WIDTH );
        }
    }

    private String getPoints(int value) {
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

    private boolean isEnemyOnPlatform(Enemy e, Platform p) {
        return e.getBounds().overlaps(p.getBounds());
    }

    private boolean isPlayerOnPlatform(Platform p) {
        return player.getJumpVelocity() < 0 && player.getBottomBound().overlaps(p.getBounds()) && !(player.getY() < p.getY());
    }
}
