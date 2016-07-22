package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.com.mygdx.game.controllers.Enemy;

/**
 * Created by pawel_000 on 2016-06-09.
 */
public class Worm extends Enemy {
    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;

    private Animation enemyRun;
    private Animation enemyDie;

    public Worm(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

    }

    public void initAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            frames.add(region);
        }

        enemyRun = new Animation(0.1f, frames);
        frames.clear();

        TextureRegion region = new TextureRegion(texture, 2 * WIDTH, 0, WIDTH, HEIGHT);
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
            case MOVE:
                result = enemyRun;
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

            box.setPosition(getX(), getY());
            top.setPosition(getX(), getY());
            bottom.setPosition(getX() + 1, getY() - getHeight() + 1);
            left.setPosition(getX(), getY() - 5);
            right.setPosition(getX() + getWidth() - 5, getY() - 5);

            animation.setPosition(this.getX(), this.getY());
        } else
            animation.setAnimation(enemyDie);
    }

    private enum State {MOVE, DIE}
}
