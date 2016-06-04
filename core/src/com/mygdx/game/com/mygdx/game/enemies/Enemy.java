package com.mygdx.game.com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.com.mygdx.game.entities.AnimatedImage;

/**
 * Created by pawel_000 on 2016-05-29.
 */
public class Enemy extends Image {
    public static enum State { MOVE, DIE };

    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;

    private boolean move = true;

    public float SPEED = 20;

    private Animation enemyRun;
    private Animation enemyDie;

    private Rectangle bounds;
    private Rectangle top;
    private Rectangle bottom;
    private Rectangle left;
    private Rectangle right;

    public AnimatedImage enem;

    private static final Texture enemyTexture = new Texture("assets/mob_1_red.png");

    public Enemy(float x, float y){
        super(new Texture("assets/mob_1_red.png"));

        this.setSize(WIDTH, HEIGHT);
        this.setPosition(x, y);
        
        initAnimations();

        enem = new AnimatedImage(getAnimation());
        bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        top = new Rectangle((int)getX() + 5, (int)getY(), (int)getWidth() - 10, 5);
        bottom = new Rectangle((int)getX() + 5, (int)getY() - (int)getHeight() + 5, (int)getWidth() - 10, 5);
        left = new Rectangle((int)getX(), (int)getY() - 5, 5, (int)getHeight() - 10);
        right = new Rectangle((int)getX() + (int)getWidth() - 5, (int)getY() - 5, 5, (int)getHeight() - 10);
    }

    private void initAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i=0 ; i<2 ; i++) {
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

    private Enemy.State getState() {
        if(move)
            return State.MOVE;
        else
            return State.DIE;
    }

    public Animation getAnimation(){
        Animation result = enemyRun;

        switch (getState()){
            case MOVE: {
                result = enemyRun;
            }break;

            case DIE:
            default:
                result = enemyDie;
                break;
        }

        return result;
    }

    public void update(){
        if(move) {
            this.moveBy(SPEED * Gdx.graphics.getDeltaTime(), 0);
            bounds.setPosition(getX(), getY());

            top.setPosition((int)getX() + 5, (int)getY());
            bottom.setPosition((int)getX() + 5, (int)getY() - (int)getHeight() + 5);
            left.setPosition((int)getX(), (int)getY() - 5);
            right.setPosition((int)getX() + (int)getWidth() - 5, (int)getY() - 5);

            enem.setPosition(this.getX(), this.getY());
        } else
            enem.setAnimation(enemyDie);
    }

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

    public void oppositeSPEED(){

        SPEED = -SPEED;
    }

    public void setMove(boolean move){
        this.move = move;
    }
}
