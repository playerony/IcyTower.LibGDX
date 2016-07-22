package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.com.mygdx.game.controllers.Enemy;

/**
 * Created by pawel_000 on 2016-07-20.
 */
public class Plant extends Enemy {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 60;
    private static float MULTIPLER_VERTICAL = 1;
    Movement move;
    private float START_X = 0;
    private float START_Y = 0;
    private boolean jump = false;
    private boolean vertical = false;
    private Animation plantMove;
    private float timer = 0.0f;

    public Plant(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

        START_X = x;
        START_Y = y;

        move = Movement.TOP;
        non_invasive = false;
    }

    public void initAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            frames.add(region);
        }

        plantMove = new Animation(0.3f, frames);
        frames.clear();
    }

    @Override
    public void die() {

        setY(START_Y);
    }

    public Animation getAnimation() {
        return plantMove;
    }

    public void oppositeSPEED() {

        SPEED = -SPEED;
    }

    public void update() {
        if (vertical)
            MULTIPLER_VERTICAL = 1;
        else
            MULTIPLER_VERTICAL = -1;

        if (getY() > START_Y && !vertical)
            this.moveBy(0, SPEED * MULTIPLER_VERTICAL * Gdx.graphics.getDeltaTime());

        else if (getY() < START_Y + HEIGHT && vertical)
            this.moveBy(0, SPEED * MULTIPLER_VERTICAL * Gdx.graphics.getDeltaTime());

        timer += Gdx.graphics.getDeltaTime();

        if (timer > 30.0f) {
            vertical = !vertical;

            timer = 0;
        }

        box.setPosition(getX(), getY());
        top.setPosition(getX(), getY());
        bottom.setPosition(getX() + 1, getY() - getHeight() + 1);
        left.setPosition(getX(), getY() - 5);
        right.setPosition(getX() + getWidth() - 5, getY() - 5);

        animation.setPosition(getX(), getY());
    }

    private enum Movement {TOP, DOWN}
}
