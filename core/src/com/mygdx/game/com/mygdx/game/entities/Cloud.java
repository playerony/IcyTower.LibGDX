package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by pawel_000 on 2016-05-26.
 */
public class Cloud extends Image{
    private static final int WIDTH = 128;
    private static final int HEIGHT = 96;

    private static float SPEED = 0.0f;

    public Cloud(float x, float y) {
        super(new Texture("assets/cloud.png"));

        this.setPosition(x, y);
        this.setSize(WIDTH, HEIGHT);

        int direction = MathUtils.random(2);

        SPEED = 100;
    }

    public void setNewSpeed(){
        SPEED = 100;
    }

    public void update(){
        this.moveBy(SPEED * Gdx.graphics.getDeltaTime(), 0);
    }
}
