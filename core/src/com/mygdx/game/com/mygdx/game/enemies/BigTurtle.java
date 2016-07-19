package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.com.mygdx.game.controllers.Enemy;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;

/**
 * Created by pawel_000 on 2016-07-19.
 */
public class BigTurtle extends Enemy {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 60;

    private boolean hide = false;

    private Animation enemyRunLeft;
    private Animation enemyRunRight;
    private Animation enemyDie;

    public BigTurtle(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT);

        dragon = true;
        SPEED = 20;
    }

    protected void initAnimations() {
        initLeftSideAnimation();
        initRightSideAnimation();
        initDieAnimation();
    }

    public void initEnemy() {
        animation = new AnimatedImage(getAnimation());

        top = new Rectangle(getX() + 1, getY(), WIDTH - 2, 10);
        bottom = new Rectangle(getX() + 1, getY() - 137 - 10, WIDTH - 2, 10);
        left = new Rectangle(getX(), getY() - 5, 10, 137 - 10);
        right = new Rectangle(getX() + WIDTH - 10, getY() - 5, 10, 137 - 10);
    }

    private void initDieAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        TextureRegion region = new TextureRegion(texture, 3 * WIDTH, 0, WIDTH, HEIGHT);
        frames.add(region);

        enemyDie = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initRightSideAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 3; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            region.flip(true, false);
            frames.add(region);
        }

        enemyRunRight = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initLeftSideAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 3; i++) {
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

    private Enemy.State getState() {
        if (move && !hide)
            return Enemy.State.MOVE;

        else if (move && hide)
            return Enemy.State.DIE;

        else
            return Enemy.State.DIE;
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
