package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.enemies.Plant;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;
import com.mygdx.game.com.mygdx.game.entities.Pipe;
import com.mygdx.game.com.mygdx.game.entities.Platform;
import com.mygdx.game.com.mygdx.game.entities.Player;
import com.mygdx.game.com.mygdx.game.screens.MenuScreen;

import java.util.ArrayList;

/**
 * Created by pawel_000 on 2016-07-21.
 */
public class PlatformsControler {
    private static final int HEIGHT_BETWEEN_PLATFORMS = 200;
    private static final int SPECIAL_MAX_LENGTH_OF_PLATFORM = 10;
    private static final int PLATFORMS = 14;
    private static int LEVEL_PLATFORMS = 1;
    private static int NUMBER_OF_BLOCK = 0;
    private static int LAST_NUMBER_OF_BLOCK = 0;
    public EnemyControler enemyControler;
    private Stage stage;
    private IcyTower game;
    private ArrayList<Pipe> pipeArray;
    private ArrayList<Platform> platformArray;

    public PlatformsControler(Stage stage, IcyTower game) {
        this.stage = stage;
        this.game = game;

        init();
    }

    public static int getLevelPlatforms() {
        return LEVEL_PLATFORMS;
    }

    private void init() {
        enemyControler = new EnemyControler(game);

        initPlatforms();

        pipeArray = new ArrayList<>();
    }

    private void initPlatforms() {
        platformArray = new ArrayList<>();

        for (int i = LEVEL_PLATFORMS; i < PLATFORMS; i++) {
            int length = MathUtils.random(2) + 3;

            Platform p = new Platform(MathUtils.random(IcyTower.SCREEN_WIDTH - length * 64),
                    LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS,
                    length, game.assets);

            LEVEL_PLATFORMS++;

            p.onStage(stage);
            platformArray.add(p);
        }
    }

    public void platformUpdate(Player player, ScoreControler scoreControler, AnimatedImage anim, MenuScreen menuScreen) {
        for (Platform p : platformArray) {
            if (isPlayerOnPlatform(player, p) && !player.getDie()) {
                player.setJump(true);
                player.setJumpVelocity(0);
                player.setY(p.getY() + p.getHeight() - 4);
                player.setCollision(true);
                player.setOnPipe(false);

                if (scoreControler.isAddPoints() && p.isPoints()) {
                    scoreControler.increaseScoreToAdd(EnemyControler.getAmoutOfEnemies() * LEVEL_PLATFORMS * 2);
                    scoreControler.setAddPoints(false);

                    p.setPoints(false);
                }
            }

            if (player.getY() - IcyTower.SCREEN_HEIGHT * 1.2f > p.getY()) {
                int length = 0;
                int value = 0;
                boolean pipe = false;

                if (LEVEL_PLATFORMS % 10 != 0) {
                    length = MathUtils.random(2) + 3;
                    value = MathUtils.random(1, 5);
                    float x = MathUtils.random(IcyTower.SCREEN_WIDTH - (length * 64));

                    p.remove();

                    if (value == 3 && length >= 4) {
                        pipe = true;
                        Pipe pip = new Pipe(game.assets.manager.get("assets/pipe.png", Texture.class), x + MathUtils.random((length * 64) - 100), LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS + 64 - 4);

                        Enemy e = new Plant(pip.getX() + pip.getWidth() / 2 - 20, pip.getY() + 40 - 5, game.assets.manager.get("assets/plant.png", Texture.class));
                        stage.addActor(e.animation);
                        enemyControler.enemyArray.add(e);

                        pipeArray.add(pip);
                        stage.addActor(pip);
                    }

                    if (LEVEL_PLATFORMS >= 20)
                        p.setBricks(NUMBER_OF_BLOCK);

                    p.setSIZE(length);
                    p.setPos(x, LEVEL_PLATFORMS * HEIGHT_BETWEEN_PLATFORMS);
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

                if (pipe)
                    LEVEL_PLATFORMS += 2;
                else
                    LEVEL_PLATFORMS++;

                if (LEVEL_PLATFORMS % 2 == 0 && EnemyControler.getAmoutOfEnemies() <= 26)
                    EnemyControler.setAmoutOfEnemies(EnemyControler.getAmoutOfEnemies() + 1);

                if (length != SPECIAL_MAX_LENGTH_OF_PLATFORM)
                    enemyControler.getRandomEnemy(p, stage, LEVEL_PLATFORMS);

                else
                    enemyControler.addDragonOnStage(p, stage, LEVEL_PLATFORMS, NUMBER_OF_BLOCK);
            }

            enemyControler.enemyUpdate(p, player, scoreControler, menuScreen, LEVEL_PLATFORMS);
        }
    }

    public void pipeUpdate(Player player, ScoreControler scoreControler) {
        Pipe pipe = null;

        for (Pipe p : pipeArray) {
            if (isPlayerOnPipe(player, p) && !player.getDie()) {
                player.setJump(true);
                player.setJumpVelocity(0);
                player.setY(p.getY() + p.getHeight());
                player.setCollision(true);
                player.setOnPipe(true);

                if (scoreControler.isAddPoints() && p.isPoints()) {
                    scoreControler.increaseScoreToAdd(EnemyControler.getAmoutOfEnemies() * LEVEL_PLATFORMS);
                    scoreControler.setAddPoints(false);

                    p.setPoints(false);
                }
            }

            if (player.getTopBound().overlaps(p.getBottomBound()) && !player.getOnPipe() && !player.getDie())
                player.setJumpVelocity(-50);

            else if (player.getRightBound().overlaps(p.getBounds()) && !player.getOnPipe() && !player.getDie())
                player.setX(p.getX() - player.getWidth());

            else if (player.getLeftBound().overlaps(p.getBounds()) && !player.getOnPipe() && !player.getDie())
                player.setX(p.getX() + p.getWidth());


            if (player.getY() - IcyTower.SCREEN_HEIGHT * 1.2f > p.getY())
                pipe = p;
        }

        if (pipe != null) {
            pipe.addAction(Actions.removeActor());
            pipe.remove();
            pipeArray.remove(pipe);
        }
    }

    private void randomNewStage() {
        do {
            LAST_NUMBER_OF_BLOCK = NUMBER_OF_BLOCK;
            NUMBER_OF_BLOCK = MathUtils.random(3);
        } while (LAST_NUMBER_OF_BLOCK == NUMBER_OF_BLOCK);
    }

    public void clearAllPlatforms() {
        LEVEL_PLATFORMS = 1;

        for (Platform p : platformArray) {
            p.removeBlocks();
            p.addAction(Actions.removeActor());
        }

        platformArray.clear();
    }

    public void clearAllPipes() {
        for (Pipe p : pipeArray) {
            p.addAction(Actions.removeActor());
            p.remove();
        }

        pipeArray.clear();
    }

    private boolean isPlayerOnPlatform(Player player, Platform p) {
        return player.getJumpVelocity() < 0 && player.getBottomBound().overlaps(p.getBounds()) && !(player.getY() < p.getY());
    }

    private boolean isPlayerOnPipe(Player player, Pipe p) {
        return player.getJumpVelocity() < 0 && player.getBottomBound().overlaps(p.getBounds()) && !(player.getY() < p.getY());
    }
}
