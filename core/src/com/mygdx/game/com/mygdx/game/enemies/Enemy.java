package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;

/**
 * Created by pawel_000 on 2016-05-29.
 */
public abstract class Enemy extends Image {
    protected static final int WIDTH = 48;
    protected static final int HEIGHT = 48;
    protected static Texture enemyTexture;
    public AnimatedImage enem;
    protected float SPEED = 10;
    protected boolean move = true;
    protected Rectangle top;
    protected Rectangle bottom;
    protected Rectangle left;
    protected Rectangle right;

    protected Enemy(float x, float y, final Texture texture) {
        super(texture);

        enemyTexture = texture;

        this.setSize(WIDTH, HEIGHT);
        this.setPosition(x, y);

        initAnimations();

        enem = new AnimatedImage(getAnimation());

        top = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), 5);
        bottom = new Rectangle((int) getX() + 1, (int) getY() - (int) getHeight() - 1, (int) getWidth() - 2, 5);
        left = new Rectangle((int)getX(), (int)getY() - 5, 5, (int)getHeight() - 10);
        right = new Rectangle((int)getX() + (int)getWidth() - 5, (int)getY() - 5, 5, (int)getHeight() - 10);
    }

    abstract void init();

    abstract void initAnimations();

    public abstract void die();

    public abstract Animation getAnimation();

    public abstract void update();

    public abstract void oppositeSPEED();

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

    public void setMove(boolean move){
        this.move = move;
    }

    //////////// SETTERS

    public float getSPEED() {
        return this.SPEED;
    }

    public void setSPEED(float speed) {
        this.SPEED = speed;
    }

}
