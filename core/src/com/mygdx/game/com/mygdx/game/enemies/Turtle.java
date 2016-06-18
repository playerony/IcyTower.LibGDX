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

    private static final float GRAVITY = -(9.81f * 2.5f);
    private boolean direction = true;
    private boolean hide = false;
    private Animation enemyRunLeft;
    private Animation enemyRunRight;
    private Animation enemyDie;

    public Turtle(float x, float y, final Texture texture) {
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

        enemyRunLeft = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 2; i++) {
            TextureRegion region = new TextureRegion(enemyTexture, i * WIDTH, 0, WIDTH, HEIGHT);
            region.flip(true, false);
            frames.add(region);
        }

        enemyRunRight = new Animation(0.1f, frames);
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

        enem.setAnimation(getAnimation());
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

    public enum State {MOVE, DIE}
}
