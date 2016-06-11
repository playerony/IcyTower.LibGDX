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
    public float SPEED = 10;

    public AnimatedImage enem;
    protected boolean move = true;

    protected Rectangle bounds;
    protected Rectangle top;
    protected Rectangle bottom;
    protected Rectangle left;
    protected Rectangle right;

    public Enemy(float x, float y, final Texture texture) {
        super(texture);

        this.setSize(WIDTH, HEIGHT);
        this.setPosition(x, y);

        initAnimations();

        enem = new AnimatedImage(getAnimation());
        bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        top = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), 5);
        bottom = new Rectangle((int) getX() + 1, (int) getY() - (int) getHeight() - 1, (int) getWidth() - 2, 5);

        left = new Rectangle((int)getX(), (int)getY() - 5, 5, (int)getHeight() - 10);
        right = new Rectangle((int)getX() + (int)getWidth() - 5, (int)getY() - 5, 5, (int)getHeight() - 10);
    }

    abstract void init();

    abstract void initAnimations();

    public abstract Animation getAnimation();

    public abstract void update();

    public abstract void oppositeSPEED();

    //////////// GETTERS

    public Rectangle getBounds() {
        return bounds;
    }

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

    //////////// SETTERS

    public void setMove(boolean move){
        this.move = move;
    }

}
