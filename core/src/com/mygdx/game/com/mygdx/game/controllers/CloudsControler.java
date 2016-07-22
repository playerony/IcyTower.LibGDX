package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.entities.Cloud;

/**
 * Created by pawel_000 on 2016-07-21.
 */
public class CloudsControler {
    private static int LEVEL_CLOUDS = 1;

    private Cloud cloud;
    private Stage stage;
    private IcyTower game;

    public CloudsControler(Stage stage, IcyTower game) {
        this.stage = stage;
        this.game = game;

        init();
    }

    private void init() {
        cloud = new Cloud(MathUtils.random(IcyTower.SCREEN_WIDTH),
                LEVEL_CLOUDS * IcyTower.SCREEN_HEIGHT + MathUtils.random(IcyTower.SCREEN_HEIGHT + 300) + 300,
                game.assets.manager.get("assets/cloud.png", Texture.class));

        LEVEL_CLOUDS++;

        stage.addActor(cloud);
    }

    public void update(OrthographicCamera camera) {
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

    public void clearClouds() {
        LEVEL_CLOUDS = 1;

        cloud.addAction(Actions.removeActor());
        cloud.remove();

        cloud.clear();
    }
}
