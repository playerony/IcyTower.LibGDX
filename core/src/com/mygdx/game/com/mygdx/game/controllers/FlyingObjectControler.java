package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.AssetsManager.Asset;
import com.mygdx.game.com.mygdx.game.entities.Player;
import com.mygdx.game.com.mygdx.game.flyingObjects.Bird;
import com.mygdx.game.com.mygdx.game.flyingObjects.Bomb;
import com.mygdx.game.com.mygdx.game.flyingObjects.Medusa;

import java.util.ArrayList;

/**
 * Created by pawel_000 on 2016-06-21.
 */
public class FlyingObjectControler {
    private static final int MIN_RANGE_SPAWN_TIME = 5;
    private static final int MAX_RANGE_SPAWN_TIME = 10;

    private static final int START_ENEMY_X_POSITION = 0;

    private ArrayList<FlyingObject> flyingObjectArray;
    private Asset assets;
    private Stage stage;

    private boolean randomize = false;

    private float spawnTimer = 0;

    public FlyingObjectControler(Asset asset, Stage stage) {
        this.assets = asset;
        this.stage = stage;

        flyingObjectArray = new ArrayList<>();
        randomSpawnTime();

        init();
    }

    private void init() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        addObjectOnStage();
                        randomSpawnTime();
                        init();
                    }
                }, spawnTimer);
            }
        }, 0, 5);
    }

    public void update(Player player) {
        FlyingObject obj = null;

        for (FlyingObject object : flyingObjectArray) {
            checkCollision(player, object);
            object.update();

            if (!object.getDirection() && object.getX() < -object.getWidth())
                obj = object;
            else if (object.getDirection() && object.getX() > IcyTower.SCREEN_WIDTH)
                obj = object;
        }

        if (obj != null) {
            obj.animation.addAction(Actions.removeActor());
            flyingObjectArray.remove(obj);
        }
    }

    private void checkCollision(Player player, FlyingObject object) {
        if (player.getRectangleBox().overlaps(object.getRectangleBox()) && !player.getDie()) {
            player.setDie(true);
            player.setJump(false);
            player.setJumpVelocity(300);
        }
    }

    private void addObjectOnStage() {
        if (flyingObjectArray.size() < 2 && !randomize) {
            FlyingObject object = randomObject();
            flyingObjectArray.add(object);
            stage.addActor(object.animation);
        }
    }

    private FlyingObject randomObject() {
        int objectType = MathUtils.random(8);
        FlyingObject object = null;

        switch (objectType) {
            case 0:
                object = new Medusa(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/medusa_pink.png", Texture.class));
                break;

            case 1:
                object = new Bomb(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/bomb_black.png", Texture.class));
                break;

            case 2:
                object = new Bird(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/bird_green.png", Texture.class));
                break;

            case 3:
                object = new Medusa(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/medusa_blue.png", Texture.class));
                break;

            case 4:
                object = new Bomb(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/bomb_blue.png", Texture.class));
                break;

            case 5:
                object = new Bird(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/bird_blue.png", Texture.class));
                break;

            case 6:
                object = new Medusa(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/medusa_white.png", Texture.class));
                break;

            case 7:
                object = new Bomb(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/bomb_grey.png", Texture.class));
                break;

            default:
                object = new Bird(START_ENEMY_X_POSITION, getRandomYPosition(), assets.manager.get("assets/bird_red.png", Texture.class));
                break;
        }

        return object;
    }

    public void clearObjects() {
        for (int i = 0; i < flyingObjectArray.size(); i++) {
            FlyingObject obj = null;

            for (FlyingObject object : flyingObjectArray) {
                obj = object;

                break;
            }

            if (obj != null) {
                obj.animation.addAction(Actions.removeActor());
                flyingObjectArray.remove(obj);
            }
        }
    }

    private float getRandomYPosition() {
        float position = Math.abs(MathUtils.random(Math.abs(stage.getCamera().position.y - IcyTower.SCREEN_HEIGHT / 4),
                stage.getCamera().position.y + IcyTower.SCREEN_HEIGHT * 2));

        return position;
    }

    private void randomSpawnTime() {
        spawnTimer = Math.abs(MathUtils.random(MIN_RANGE_SPAWN_TIME, MAX_RANGE_SPAWN_TIME));
    }

    public void setRandomize(boolean randomize) {
        this.randomize = randomize;
    }
}
