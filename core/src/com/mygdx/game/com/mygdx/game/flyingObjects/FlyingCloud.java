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
 * Created by pawel_000 on 2016-07-19.
 */
public class FlyingCloud extends FlyingObject {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 60;

    private static final float ANIMATION_SPEED = 0.6f;

    private static float MULTIPLER_LEVEL = 1;
    private static float MULTIPLER_VERTICAL = 1;
    private static float timer = 0.0f;

    private boolean jump = false;
    private boolean vertical = false;

    public FlyingCloud(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

        init();
    }

    private void init() {
        SPEED = 50 + MathUtils.random(20);
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

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            region.flip(true, false);
            frames.add(region);
        }

        animation = new AnimatedImage(new Animation(ANIMATION_SPEED, frames));
        frames.clear();
    }

    private void initLeftAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        ///////////////////////////

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            frames.add(region);
        }

        animation = new AnimatedImage(new Animation(ANIMATION_SPEED, frames));
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
        if (!direction)
            this.moveBy(-SPEED * MULTIPLER_LEVEL * Gdx.graphics.getDeltaTime(), 0);
        else
            this.moveBy(SPEED * MULTIPLER_LEVEL * Gdx.graphics.getDeltaTime(), 0);

        if (jump) {
            if (MULTIPLER_VERTICAL == 1)
                vertical = MathUtils.randomBoolean();

            if (vertical)
                MULTIPLER_VERTICAL = 2;
            else
                MULTIPLER_VERTICAL = -2;

            this.moveBy(0, -GRAVITY * MULTIPLER_VERTICAL * 3 * Gdx.graphics.getDeltaTime());
        } else
            MULTIPLER_VERTICAL = 1;

        timer += Gdx.graphics.getDeltaTime();

        if (timer > 1.0f) {
            jump = !jump;

            timer = 0;
        }

        animation.setPosition(this.getX(), this.getY());
        box.setPosition(getX(), getY());
    }
}
