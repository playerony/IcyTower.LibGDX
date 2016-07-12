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
public class Bird extends FlyingObject {
    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;

    private float timer = 0.0f;
    private float time = 0.0f;
    private float jumpVelocity = 0.0f;

    public Bird(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

        init();
    }

    private void init() {
        SPEED = 70 + MathUtils.random(30);
        box = new Rectangle((int) getX(), (int) getY(), WIDTH, HEIGHT);

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

        for (int i = 0; i < 2; i++) {
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

        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT));

        animation = new AnimatedImage(new Animation(0.1f, frames));
        frames.clear();
    }

    public void update() {
        if (!direction)
            this.moveBy(-SPEED * Gdx.graphics.getDeltaTime(), jumpVelocity * Gdx.graphics.getDeltaTime());
        else
            this.moveBy(SPEED * Gdx.graphics.getDeltaTime(), jumpVelocity * Gdx.graphics.getDeltaTime());

        jumpVelocity += GRAVITY;
        timer += Gdx.graphics.getDeltaTime();

        if (timer > time) {
            jumpVelocity = 400;

            timer = 0;
            time = MathUtils.random(0.4f, 0.8f);
        }

        animation.setPosition(this.getX(), this.getY());
        box.setPosition((int) getX(), (int) getY());
    }

    protected void initPosition() {
        int side = MathUtils.random(1);
        float x = this.getX();
        float y = this.getY();

        if (side == 0) {
            direction = false;

            x = IcyTower.SCREEN_WIDTH;
        } else {
            direction = true;

            x = -WIDTH;
        }

        this.setPosition(x, y);
        animation.setPosition(x, y);
    }

    public Animation getAnimation() {
        return null;
    }
}