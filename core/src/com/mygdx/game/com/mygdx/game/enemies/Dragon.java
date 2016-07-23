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
 * Created by pawel_000 on 2016-07-15.
 */
public class Dragon extends Enemy {
    private static final int WIDTH = 95;
    private static final int HEIGHT = 95;

    private boolean hide = false;

    private Animation enemyRunLeft;
    private Animation enemyRunRight;
    private Animation enemyDie;

    public Dragon(float x, float y, final Texture texture) {
        super(texture, x, y, WIDTH, HEIGHT - 30);

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

        box = new Rectangle(getX(), getY(), getWidth(), 150);
        top = new Rectangle(getX(), getY() + 64, getWidth(), 40);
        bottom = new Rectangle(getX() + 1, getY() - HEIGHT - 1, getWidth() - 2, 5);
        left = new Rectangle(getX(), getY() - 5, 5, HEIGHT);
        right = new Rectangle(getX() + getWidth() - 5, getY() - 5, 5, HEIGHT);
    }

    private void initDieAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        TextureRegion region = new TextureRegion(texture, (3 * WIDTH) + 1, 0, WIDTH + 1, HEIGHT);
        frames.add(region);

        enemyDie = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initRightSideAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        TextureRegion region = null;

        for (int i = 0; i < 3; i++) {
            if (i == 1)
                region = new TextureRegion(texture, i * WIDTH, 0, WIDTH + 1, HEIGHT);
            else if (i == 0)
                region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            else
                region = new TextureRegion(texture, (i * WIDTH) + 1, 0, WIDTH, HEIGHT);

            region.flip(true, false);
            frames.add(region);
        }

        enemyRunRight = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initLeftSideAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        TextureRegion region = null;

        for (int i = 0; i < 3; i++) {
            if (i == 1)
                region = new TextureRegion(texture, i * WIDTH, 0, WIDTH + 1, HEIGHT);
            else if (i == 0)
                region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            else
                region = new TextureRegion(texture, (i * WIDTH) + 1, 0, WIDTH, HEIGHT);

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

            box.setPosition(getX(), getY());
            top.setPosition(getX(), getY());
            bottom.setPosition(getX() + 1, getY() - getHeight() + 1);
            left.setPosition(getX(), getY() - 5);
            right.setPosition(getX() + getWidth() - 5, getY() - 5);

            animation.setPosition(this.getX(), this.getY());
        } else
            animation.setAnimation(enemyDie);
    }
}
