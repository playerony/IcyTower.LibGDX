package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.com.mygdx.game.entities.Entity;

/**
 * Created by pawel_000 on 2016-06-19.
 */
public abstract class FlyingObject extends Entity {
    protected float SPEED = 50;

    protected Animation objectMoveAnimation;
    protected Rectangle box;

    public FlyingObject(final Texture texture, float x, float y, int WIDTH, int HEIGHT) {
        super(texture, x, y, WIDTH, HEIGHT);

        initAnimations();
        initPosition();
    }

    protected abstract void initAnimations();

    public abstract void update();

    protected abstract void initPosition();

    protected boolean getDirection() {
        return direction;
    }

    protected Rectangle getRectangleBox() {
        return box;
    }
}
