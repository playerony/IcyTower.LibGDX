package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.enemies.Enemy;
import com.mygdx.game.com.mygdx.game.entities.*;

import java.util.ArrayList;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class GameScreen extends AbstractScreen {

    private static final int HEIGHT_BETWEEN_PLATFORMS = 200;
    private int CLOUDS = 10;
    private static final int PLATFORMS = 12;

    private static int LEVEL_CLOUDS = 1;
    private static int LEVEL_PLATFORMS = 1;

    private BitmapFont font;
    private static int SCORE = 0;
    private static int add = 0;
    private static int value = 0;

    private boolean addPoints = true;

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
        init();
    }

    private void init(){
        initClouds();
        initPlatforms();
        initFont();
        initEnemy();

        initBackground();
        initFloor();

        initPlayer();
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
            c = new Cloud(MathUtils.random(game.SCREEN_WIDTH), LEVEL_CLOUDS * IcyTower.SCREEN_HEIGHT + MathUtils.random(IcyTower.SCREEN_HEIGHT + 300) + 300);

            LEVEL_CLOUDS++;

            stage.addActor(c);
            cloudArray.add(c);
        }
    }

    private void initPlayer() {
        player = new Player();
        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + 250, 0);
        cameraScore.position.set(camera.position.x + 70, camera.position.y, 0);

        anim = new AnimatedImage(player.getAnimation());

        anim.setPosition(player.getX(), player.getY());
        anim.setAnimation(player.getAnimation());

        stage.addActor(anim);
    }

    private void initPlatforms() {
        platformArray = new ArrayList<>();

        for(int i=1 ; i<PLATFORMS ; i++) {
            int length = MathUtils.random(3) + 3;

            Platform p = new Platform(MathUtils.random(game.SCREEN_WIDTH - length * 64), LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS, length);
            p.onStage(stage);

            LEVEL_PLATFORMS++;

            platformArray.add(p);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        
        update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();
        batch.end();

        batch.begin();
        batch.setProjectionMatrix(cameraScore.combined);

        if(SCORE == 0)
            font.draw(batch, "MARIO\n" + "000000", 0, IcyTower.SCREEN_HEIGHT - font.getXHeight());
        else if(SCORE < 9 && SCORE > 0)
            font.draw(batch, "MARIO\n" + "00000" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight());
        else if(SCORE < 99 && SCORE >= 10)
            font.draw(batch, "MARIO\n" + "0000" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight());
        else if(SCORE < 999 && SCORE >= 100)
            font.draw(batch, "MARIO\n" + "000" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight());
        else if(SCORE < 9999 && SCORE >= 1000)
            font.draw(batch, "MARIO\n" + "00" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight());
        else if(SCORE < 99999 && SCORE >= 10000)
            font.draw(batch, "MARIO\n" + "0" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight());
        else
            font.draw(batch, "MARIO\n" + Integer.toString(SCORE), 0, IcyTower.SCREEN_HEIGHT - font.getXHeight());

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

        camera.position.set(IcyTower.SCREEN_WIDTH / 2, player.getY() + 200 + player.getJumpVelocity() / 100, 0);

        for(Platform p : platformArray) {
            if(isPlayerOnPlatform(p)){
                player.setJump(true);
                player.setJumpVelocity(0);
                player.setY(p.getY() + p.getHeight() - 4);
                player.setCollision(true);

                if(addPoints) {
                    add += (2 * LEVEL_PLATFORMS);
                    addPoints = false;
                }
            }

            if(player.getY() - IcyTower.SCREEN_HEIGHT * 2 > p.getY()){
                int length = MathUtils.random(3) + 3;

                p.setSIZE(length);
                p.setPos(stage, MathUtils.random(game.SCREEN_WIDTH - (length * 64)), LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS);

                LEVEL_PLATFORMS++;

                int value = MathUtils.random(6);

                if(value < 3){
                    Enemy e;
                    e = new Enemy(p.getX() + 15, p.getY() + p.getHeight() - 4);
                    e.enem.setAnimation(e.getAnimation());
                    e.enem.setPosition(e.getX(), e.getY());

                    stage.addActor(e.enem);
                    enemyArray.add(e);
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
            for(Enemy e : removeEnemyArray){
                e.enem.clear();

                enemyArray.remove(e);
            }

            removeEnemyArray.clear();
        }
    }

    private void enemyUpdate(Platform p) {
        for(Enemy e : enemyArray) {
            e.update();

            if(isEnemyOnPlatform(e, p) && (e.getX() > p.getX() + p.getBounds().getWidth() - e.getBounds().getWidth() || e.getX() < p.getX()))
                e.SPEED = -e.SPEED;

            if(player.getBottomBound().overlaps(e.getTopBound())) {
                e.setMove(false);
                removeEnemyArray.add(e);
            }

            if(player.getRightBound().overlaps(e.getLeftBound())){
                player.setJumpVelocity(300);
                e.SPEED = -e.SPEED;
            }

            if(player.getLeftBound().overlaps(e.getRightBound())){
                player.setJumpVelocity(300);
                e.SPEED = -e.SPEED;
            }

            if(player.getTopBound().overlaps(e.getBottomBound())){
                player.setJumpVelocity(-800);
            }
        }
    }

    private void cloudsUpdate() {
        for(Cloud c : cloudArray){
            c.update();

            if(player.getY() - IcyTower.SCREEN_HEIGHT > c.getY()){
                c.setPosition(MathUtils.random(game.SCREEN_WIDTH), LEVEL_CLOUDS * IcyTower.SCREEN_HEIGHT + MathUtils.random(IcyTower.SCREEN_HEIGHT + 300) + 300);
                c.setNewSpeed();

                LEVEL_CLOUDS++;
            }

            if(c.getX() >= IcyTower.SCREEN_WIDTH)
                c.setX( -c.getWidth() );

            else if(c.getX() <= -c.getWidth())
                c.setX( IcyTower.SCREEN_WIDTH );
        }
    }

    private boolean isEnemyOnPlatform(Enemy e, Platform p) {
        return e.getBounds().overlaps(p.getBounds());
    }

    private boolean isPlayerOnPlatform(Platform p) {
        return player.getJumpVelocity() < 0 && player.getBounds().overlaps(p.getBounds()) && !(player.getY() < p.getY());
    }
}
