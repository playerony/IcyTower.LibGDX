package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.enemies.*;
import com.mygdx.game.com.mygdx.game.entities.Platform;
import com.mygdx.game.com.mygdx.game.entities.Player;
import com.mygdx.game.com.mygdx.game.screens.MenuScreen;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by pawel_000 on 2016-07-21.
 */
public class EnemyControler {

    private static int AMOUT_OF_ENEMIES = 1;
    private static float enemyTimer = 0;
    public ArrayList<Enemy> enemyArray;
    public ArrayList<Enemy> enemyToRemoveArray;
    private IcyTower game;

    public EnemyControler(IcyTower game) {
        this.game = game;

        initEnemy();
    }

    public static int getAmoutOfEnemies() {
        return AMOUT_OF_ENEMIES;
    }

    public static void setAmoutOfEnemies(int amoutOfEnemies) {
        AMOUT_OF_ENEMIES = amoutOfEnemies;
    }

    private void initEnemy() {
        enemyToRemoveArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
    }

    public void enemyUpdate(Platform p, Player player, ScoreControler scoreControler, MenuScreen menuScreen, int LEVEL_PLATFORMS) {
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
                if (player.getBottomBound().overlaps(e.getTopBound()) && e.isInvasive()) {
                    e.die();

                    if (!e.getDragonInfo()) {
                        game.getSoundService().playStompSound();
                        scoreControler.increaseScoreToAdd(LEVEL_PLATFORMS * 100);
                        player.setJumpVelocity(1475);
                    } else {
                        game.getSoundService().playSuperJumpSound();

                        if (LEVEL_PLATFORMS * 5 <= 400)
                            scoreControler.increaseScoreToAdd(LEVEL_PLATFORMS * (600 - LEVEL_PLATFORMS * 5));
                        else
                            scoreControler.increaseScoreToAdd(LEVEL_PLATFORMS * 150);

                        player.setJumpVelocity(1675);
                    }

                    player.setRotate(true);
                    player.setFlip(true);

                    enemyToRemoveArray.add(e);
                }

                if (player.getRightBound().overlaps(e.getLeftBound()) ||
                        player.getLeftBound().overlaps(e.getRightBound()) ||
                        player.getTopBound().overlaps(e.getBottomBound()) ||
                        (player.getRectangleBox().overlaps(e.getBound()) && !e.isInvasive())) {

                    player.setDie(true);
                    player.setJumpVelocity(600);

                    game.getSoundService().pauseTitleSound();
                    game.getSoundService().playDeathSound();

                    try {
                        menuScreen.saveNewRecord(scoreControler);
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

    public void addDragonOnStage(Platform p, Stage stage, int LEVEL_PLATFORMS, int NUMBER_OF_BLOCK) {
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
            e.setSPEED(e.getSPEED() + (LEVEL_PLATFORMS / 13));

            stage.addActor(e.animation);
            enemyArray.add(e);
        }
    }

    public void getRandomEnemy(Platform p, Stage stage, int LEVEL_PLATFORMS) {
        int value = MathUtils.random(6);

        if (value <= 4 && enemyArray.size() <= 5 + (LEVEL_PLATFORMS / 10)) {
            Enemy e = null;
            int type = MathUtils.random(AMOUT_OF_ENEMIES);
            float START_POS_ON_PLATFORM = p.getX() + p.getWidth() / 2;

            switch (type) {
                case 0:
                    e = new Worm(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_1_red.png", Texture.class));
                    break;

                case 2:
                    e = new Turtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_2_green.png", Texture.class));
                    break;

                case 4:
                    e = new Worm(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_1_blue.png", Texture.class));
                    break;

                case 6:
                    e = new Turtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_3_black.png", Texture.class));
                    break;

                case 8:
                    e = new Worm(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_pink.png", Texture.class));
                    break;

                case 10:
                    e = new Turtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_2_red.png", Texture.class));
                    break;

                case 12:
                    e = new BigTurtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/bigturtle_green.png", Texture.class));
                    break;

                case 14:
                    e = new Hedgehog(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/hedgehog.png", Texture.class));
                    break;

                case 16:
                    e = new Turtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_3_blue.png", Texture.class));
                    break;

                case 18:
                    e = new Worm(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_black.png", Texture.class));
                    break;

                case 20:
                    e = new Worm(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_1_grey.png", Texture.class));
                    break;

                case 22:
                    e = new BigTurtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/bigturtle_grey.png", Texture.class));
                    break;

                case 24:
                    e = new Worm(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_blue.png", Texture.class));
                    break;

                case 26:
                    e = new Turtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_2_blue.png", Texture.class));
                    break;

                case 28:
                    e = new Turtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/mob_3_grey.png", Texture.class));
                    break;

                case 30:
                    e = new Worm(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/deal_grey.png", Texture.class));
                    break;

                case 32:
                    e = new BigTurtle(START_POS_ON_PLATFORM, p.getY() + p.getHeight() - 4, game.assets.manager.get("assets/bigturtle_blue.png", Texture.class));
                    break;

                default:

                    break;
            }

            if (e != null) {
                e.animation.setAnimation(e.getAnimation());
                e.animation.setPosition(e.getX(), e.getY());
                e.setSPEED(e.getSPEED() + (LEVEL_PLATFORMS / 13));

                stage.addActor(e.animation);
                enemyArray.add(e);
            }
        }
    }

    private void enemyDie() {
        if (enemyToRemoveArray != null) {
            enemyTimer += Gdx.graphics.getDeltaTime();

            if (enemyTimer > 5.0f) {
                Enemy ene = null;

                for (Enemy e : enemyToRemoveArray) {
                    ene = e;
                    break;
                }

                if (ene != null) {
                    ene.animation.addAction(Actions.removeActor());
                    ene.remove();
                    enemyToRemoveArray.remove(ene);
                }

                enemyTimer = 0;
            }
        }
    }

    public void clearAllEnemies() {
        AMOUT_OF_ENEMIES = 1;

        for (Enemy e : enemyArray) {
            e.removeEnemy();
            e.remove();
            e.addAction(Actions.removeActor());
        }

        enemyArray.clear();
    }

    private boolean isEnemyOnPlatform(Enemy e, Platform p) {
        return e.getBottomBound().overlaps(p.getBounds());
    }
}
