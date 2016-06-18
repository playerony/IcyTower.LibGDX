package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by pawel_000 on 2016-06-09.
 */
public class Worm extends Enemy {
    private Animation enemyRun;
    private Animation enemyDie;

    public Worm(float x, float y, final Texture texture) {
        super(x, y, texture);

        this.setPosition(x, y);

        init();
    }

    void init() {
        this.setSize(WIDTH, HEIGHT);
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

    @Override
    public void die() {
        this.setMove(false);
    }

    public Animation getAnimation() {
        Animation result = enemyRun;

        switch (getState()) {
            case MOVE: {
                result = enemyRun;
            }
            break;

            case DIE:
            default:
                result = enemyDie;
                break;
        }

        return result;
    }

    public void oppositeSPEED() {
        SPEED = -SPEED;
    }

    private State getState() {
        if (move)
            return State.MOVE;
        else
            return State.DIE;
    }

    public void update() {
        if (move) {
            this.moveBy(SPEED * Gdx.graphics.getDeltaTime(), 0);

            top.setPosition((int) getX(), (int) getY());
            bottom.setPosition((int) getX() + 1, (int) getY() - (int) getHeight() + 1);
            left.setPosition((int) getX(), (int) getY() - 5);
            right.setPosition((int) getX() + (int) getWidth() - 5, (int) getY() - 5);

            enem.setPosition(this.getX(), this.getY());
        } else
            enem.setAnimation(enemyDie);
    }

    private enum State {MOVE, DIE}
}
