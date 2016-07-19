package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.com.mygdx.game.controllers.Enemy;

/**
 * Created by pawel_000 on 2016-07-19.
 */
public class Hedgehog extends Enemy {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 34;

    private boolean hide = false;

    private Animation enemyRunLeft;
    private Animation enemyRunRight;
    private Animation enemyDie;

    public Hedgehog(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

    }

    protected void initAnimations() {
        initLeftSideAnimation();
        initRightSideAnimation();
        initDieAnimation();
    }

    private void initDieAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        TextureRegion region = new TextureRegion(texture, 2 * WIDTH, 0, WIDTH, HEIGHT);
        frames.add(region);

        enemyDie = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initRightSideAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            region.flip(true, false);
            frames.add(region);
        }

        enemyRunRight = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initLeftSideAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            frames.add(region);
        }

        enemyRunLeft = new Animation(0.1f, frames);
        frames.clear();
    }

    @Override
    public void die() {
        this.setMove(false);
    }

    public Animation getAnimation() {
        Animation result = null;

        switch (getState()) {
            case MOVE: {
                if (SPEED > 0) {
                    result = enemyRunRight;
                } else {
                    result = enemyRunLeft;
                }
            }
            break;

            case DIE:
            default:
                result = enemyDie;
                break;
        }

        return result;
    }

    private State getState() {
        if (move && !hide)
            return State.MOVE;

        else if (move && hide)
            return State.DIE;

        else
            return State.DIE;
    }

    public void oppositeSPEED() {
        SPEED = -SPEED;
        direction = !direction;

        animation.setAnimation(getAnimation());
    }

    public void update() {
        if (move) {
            this.moveBy(SPEED * Gdx.graphics.getDeltaTime(), 0);

            top.setPosition(getX(), getY());
            bottom.setPosition(getX() + 1, getY() - getHeight() + 1);
            left.setPosition(getX(), getY() - 5);
            right.setPosition(getX() + getWidth() - 5, getY() - 5);

            animation.setPosition(this.getX(), this.getY());
        } else
            animation.setAnimation(enemyDie);
    }
}
