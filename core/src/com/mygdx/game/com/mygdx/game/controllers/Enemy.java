package com.mygdx.game.com.mygdx.game.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;
import com.mygdx.game.com.mygdx.game.entities.Entity;

/**
 * Created by pawel_000 on 2016-05-29.
 */
public abstract class Enemy extends Entity {

    protected boolean move = true;
    protected float SPEED = 10;

    protected Rectangle top;
    protected Rectangle bottom;
    protected Rectangle left;
    protected Rectangle right;

    protected Enemy(final Texture texture, float x, float y, final int WIDTH, final int HEIGHT) {
        super(texture, x, y, WIDTH, HEIGHT);

        init();
    }

    private void init() {
        initAnimations();
        initEnemy();
    }

    private void initEnemy() {
        animation = new AnimatedImage(getAnimation());

        top = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), 5);
        bottom = new Rectangle((int) getX() + 1, (int) getY() - (int) getHeight() - 1, (int) getWidth() - 2, 5);
        left = new Rectangle((int)getX(), (int)getY() - 5, 5, (int)getHeight() - 10);
        right = new Rectangle((int)getX() + (int)getWidth() - 5, (int)getY() - 5, 5, (int)getHeight() - 10);
    }

    protected abstract void initAnimations();

    public abstract void die();

    public abstract void update();

    public abstract void oppositeSPEED();

    public abstract Animation getAnimation();

    //////////// GETTERS

    public Rectangle getTopBound() { return top; }

    public Rectangle getBottomBound() {
        return bottom;
    }

    public Rectangle getLeftBound() {
        return left;
    }

    public Rectangle getRightBound() {
        return right;
    }

    public boolean getMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    //////////// SETTERS

    public float getSPEED() {
        return this.SPEED;
    }

    public void setSPEED(float speed) {
        this.SPEED = speed;
    }

    public enum State {MOVE, DIE}
}
