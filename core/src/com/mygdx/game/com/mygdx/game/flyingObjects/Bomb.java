package com.mygdx.game.com.mygdx.game.flyingObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.IcyTower;
import com.mygdx.game.com.mygdx.game.controllers.FlyingObject;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;

/**
 * Created by pawel_000 on 2016-06-19.
 */
public class Bomb extends FlyingObject {
    private static final int WIDTH = 64;
    private static final int HEIGHT = 56;

    public Bomb(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

        init();
    }

    private void init() {
        SPEED = 250 + MathUtils.random(50);
        box = new Rectangle(getX(), getY(), WIDTH, HEIGHT);

        initAnimations();
    }

    protected void initAnimations() {
        if (direction)
            initRightAnimation();

        else
            initLeftAnimation();
    }

    private void initRightAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        ///////////////////////////

        for (int i = 0; i < 1; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            region.flip(true, false);
            frames.add(region);
        }

        animation = new AnimatedImage(new Animation(0.1f, frames));
        frames.clear();
    }

    private void initLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        ///////////////////////////

        for (int i = 0; i < 1; i++)
            frames.add(new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT));

        animation = new AnimatedImage(new Animation(0.1f, frames));
        frames.clear();
    }

    protected void initPosition() {
        int side = MathUtils.random(1);
        float y = this.getY();

        if (side == 0) {
            direction = false;

            float x = IcyTower.SCREEN_WIDTH;

            this.setPosition(x, y);
            animation.setPosition(x, y);
        } else {
            direction = true;

            float x = -WIDTH;

            this.setPosition(x, y);
            animation.setPosition(x, y);
        }
    }

    public void update() {
        if (!direction) {
            this.moveBy(-SPEED * Gdx.graphics.getDeltaTime(), GRAVITY * Gdx.graphics.getDeltaTime());
            animation.rotateBy((-GRAVITY / SPEED) * 2 * Gdx.graphics.getDeltaTime());
        } else {
            this.moveBy(SPEED * Gdx.graphics.getDeltaTime(), GRAVITY * Gdx.graphics.getDeltaTime());
            animation.rotateBy((GRAVITY / SPEED) * 2 * Gdx.graphics.getDeltaTime());
        }

        animation.setPosition(this.getX(), this.getY());
        box.setPosition(getX(), getY());
    }
}
