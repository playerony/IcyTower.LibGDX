package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class Player extends Image {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final int START_X = 100;
    private static final int START_Y = 0;
    private static final float GRAVITY = -(9.81f * 2.5f);
    public State currentState;
    public Direction direction;
    private float jumpVelocity;
    private float runVelocity;

    private boolean collision = false;
    private boolean jump = true;
    private boolean floor = false;
    private boolean die = false;
    private Texture playerTexture;
    private Animation playerRunRight;
    private Animation playerJumpRight;
    private Animation playerStandingRight;
    private Animation playerRunLeft;
    private Animation playerJumpLeft;
    private Animation playerStandingLeft;
    private Animation playerDie;
    private Rectangle bottom;
    private Rectangle top;
    private Rectangle left;
    private Rectangle right;

    public Player(Texture texture) {
        super(texture);

        this.setSize(WIDTH, HEIGHT);
        this.setOrigin(WIDTH / 2, HEIGHT / 2);
        this.setPosition(START_X, START_Y);

        playerTexture = texture;

        init();
    }

    private void init() {
        currentState = State.STANDING;

        initRightAnimations();
        initLeftAnimations();

        top = new Rectangle((int)getX() + 5, (int)getY(), (int)getWidth() - 10, 5);
        bottom = new Rectangle((int) getX() + 1, (int) getY() - (int) getHeight() + 10, (int) getWidth() - 2, 10);
        left = new Rectangle((int)getX(), (int)getY() - 5, 5, (int)getHeight() - 10);
        right = new Rectangle((int)getX() + (int)getWidth() - 5, (int)getY() - 5, 5, (int)getHeight() - 10);

        direction = Direction.RIGHT;
    }

    private void initLeftAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        ///////////////////////////

        for(int i=1 ; i<4 ; i++) {
            TextureRegion region = new TextureRegion(playerTexture, i * 64, 0, 64, 64);
            region.flip(true, false);
            frames.add(region);
        }

        playerRunLeft = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        for(int i=5 ; i<6 ; i++) {
            TextureRegion region = new TextureRegion(playerTexture, i * 64, 0, 64, 64);
            region.flip(true, false);
            frames.add(region);
        }

        playerJumpLeft = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        TextureRegion region = new TextureRegion(playerTexture, 0, 0, 64, 64);
        region.flip(true, false);
        frames.add(region);

        playerStandingLeft = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initRightAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        ///////////////////////////

        for(int i=1 ; i<4 ; i++)
            frames.add(new TextureRegion(playerTexture, i * 64, 0, 64, 64));

        playerRunRight = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        for(int i=5 ; i<6 ; i++)
            frames.add(new TextureRegion(playerTexture, i * 64, 0, 64, 64));

        playerJumpRight = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        frames.add(new TextureRegion(playerTexture, 0, 0, 64, 64));

        playerStandingRight = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        frames.add(new TextureRegion(playerTexture, 6 * 64, 0, 64, 64));

        playerDie = new Animation(0.1f, frames);
        frames.clear();
    }

    public void update(){
        inputHandler();

        if(!collision)
            move();

        if( this.getY() > 36.5) {
            jumpVelocity += GRAVITY;
            collision = false;
        } else {
            this.setY((float) 36.5);
            jumpVelocity = 0;
            jump = true;
            floor = true;
        }

        bottom.setPosition((int) getX() + 1, (int) getY() - (int) getHeight() + 5);
        top.setPosition((int)getX() + 5, (int)getY());
        left.setPosition((int)getX(), (int)getY() - 5);
        right.setPosition((int)getX() + (int)getWidth() - 5, (int)getY() - 5);
    }

    public Animation getAnimation(){
        currentState = getState();
        Animation result = playerStandingRight;

        if(direction == Direction.RIGHT) {
            switch (currentState) {
                case JUMPING:
                    result = playerJumpRight;
                    break;

                case RUNNING:
                    result = playerRunRight;
                    break;

                case DIE:
                    result = playerDie;
                    break;

                case FALLING:
                case STANDING:
                default:
                    result = playerStandingRight;
                    break;
            }
        } else if(direction == Direction.LEFT) {
            switch (currentState) {
                case JUMPING:
                    result = playerJumpLeft;
                    break;

                case RUNNING:
                    result = playerRunLeft;
                    break;

                case DIE:
                    result = playerDie;
                    break;

                case FALLING:
                case STANDING:
                default:
                    result = playerStandingLeft;
                    break;
            }
        }

        return result;
    }

    public void move(){

        this.moveBy(0, jumpVelocity * Gdx.graphics.getDeltaTime());
    }

    private State getState() {
        if (runVelocity != 0 && (jumpVelocity == 0 || floor) && !die)
            return State.RUNNING;

        else if (jumpVelocity > 0 && !die)
            return State.JUMPING;

        else if (jumpVelocity < 0 && !die)
            return State.FALLING;

        else if (die) {
            return State.DIE;
        }

        else
            return State.STANDING;
    }

    private void inputHandler(){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            runVelocity = -1 * 500 * Gdx.graphics.getDeltaTime();

            this.moveBy(runVelocity, 0);
            direction = Direction.LEFT;
        }

        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            runVelocity = 500 * Gdx.graphics.getDeltaTime();

            this.moveBy(runVelocity, 0);
            direction = Direction.RIGHT;
        }

        else
            runVelocity = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            runVelocity = 0;
            jump();
        }
    }

    private void jump() {
        if(jump && jumpVelocity >= -100){
            jumpVelocity += 800;
            jump = false;
        }
    }

    public Rectangle getTopBound() {
        return top;
    }

    public Rectangle getBottomBound() {
        return bottom;
    }

    //////////// GETTERS

    public Rectangle getLeftBound() {
        return left;
    }

    public Rectangle getRightBound() {
        return right;
    }

    public float getJumpVelocity() {
        return jumpVelocity;
    }

    public void setJumpVelocity(float velocity) {
        this.jumpVelocity = velocity;
    }

    public boolean getDie() {
        return die;
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    //////////// SETTERS

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setCollision(boolean value) {
        collision = value;
    }

    private enum State {FALLING, JUMPING, STANDING, RUNNING, DIE}

    private enum Direction {LEFT, RIGHT}
}
