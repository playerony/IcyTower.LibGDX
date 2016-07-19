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
public class Medusa extends FlyingObject {
    private static final int WIDTH = 41;
    private static final int HEIGHT = 60;

    private static float MULTIPLER = 1.0f;
    private static float timer = 0.0f;

    private boolean jump = false;

    public Medusa(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

        init();
    }

    private void init() {
        SPEED = 40 + MathUtils.random(20);
        box = new Rectangle((int) getX(), (int) getY(), WIDTH, HEIGHT);

        initAnimations();
    }

    protected void initAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        ///////////////////////////

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            frames.add(region);
        }

        animation = new AnimatedImage(new Animation(0.2f, frames));
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
            this.moveBy(-SPEED * MULTIPLER * Gdx.graphics.getDeltaTime(), 0);
        else
            this.moveBy(SPEED * MULTIPLER * Gdx.graphics.getDeltaTime(), 0);

        if (jump) {
            if (MULTIPLER == 1)
                MULTIPLER = MathUtils.random(-1.1f, 2.5f);

            this.moveBy(0, -GRAVITY * MULTIPLER * 3 * Gdx.graphics.getDeltaTime());
        } else
            MULTIPLER = 1;

        timer += Gdx.graphics.getDeltaTime();

        if (timer > 1.0f) {
            jump = !jump;

            timer = 0;
        }

        animation.setPosition(this.getX(), this.getY());
        box.setPosition(getX(), getY());
    }
}
