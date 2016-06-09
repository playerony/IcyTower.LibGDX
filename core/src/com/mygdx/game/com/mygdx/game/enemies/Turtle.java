package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by pawel_000 on 2016-06-09.
 */
public class Turtle extends Enemy {
    private static final Texture enemyTexture = new Texture("assets/mob_2_green.png");

    public Turtle(float x, float y) {
        super(x, y, enemyTexture);

        this.setPosition(x, y);

        init();
    }

    void init() {
        this.setSize(WIDTH, HEIGHT);

        initAnimations();
    }

    void initAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(enemyTexture, i * WIDTH, 0, WIDTH, HEIGHT);
            frames.add(region);
        }

        enemyRun = new Animation(0.1f, frames);
        frames.clear();

        TextureRegion region = new TextureRegion(enemyTexture, 2 * WIDTH, 0, WIDTH, HEIGHT);
        frames.add(region);

        enemyDie = new Animation(0.1f, frames);
        frames.clear();
    }

    public void update() {
        if (move) {
            this.moveBy(SPEED * Gdx.graphics.getDeltaTime(), 0);
            bounds.setPosition(getX(), getY());

            top.setPosition((int) getX() + 5, (int) getY());
            bottom.setPosition((int) getX() + 5, (int) getY() - (int) getHeight() + 5);
            left.setPosition((int) getX(), (int) getY() - 5);
            right.setPosition((int) getX() + (int) getWidth() - 5, (int) getY() - 5);

            enem.setPosition(this.getX(), this.getY());
        } else
            enem.setAnimation(enemyDie);
    }

    public enum State {MOVE, DIE}
}
