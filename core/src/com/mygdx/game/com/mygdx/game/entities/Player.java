package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public class Player extends Entity {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final int JUMP_VELOCITY = 800;
    private static final float START_X = 25.0f;
    private static final float START_Y = 36.5f;
    public float angle = 0.0f;
    private float jumpVelocity;
    private float runVelocity;
    private State currentState;
    private Direction direction;

    private boolean collision = false;
    private boolean rotate = false;
    private boolean jump = true;
    private boolean floor = false;
    private boolean die = false;
    private boolean flip = false;

    private Animation playerRunRight;
    private Animation playerJumpRight;
    private Animation playerStandingRight;
    private Animation playerFlipRight;

    private Animation playerRunLeft;
    private Animation playerJumpLeft;
    private Animation playerStandingLeft;
    private Animation playerFlipLeft;
    private Animation playerDie;

    private Rectangle box;
    private Rectangle bottom;
    private Rectangle top;
    private Rectangle left;
    private Rectangle right;

    public Player(Texture texture) {
        super(texture, START_X, START_Y, WIDTH, HEIGHT);

        this.setOrigin(WIDTH / 2, HEIGHT / 2);

        init();
    }

    protected void init() {
        currentState = State.STANDING;

        initRightAnimations();
        initLeftAnimations();

        box = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
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
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            region.flip(true, false);
            frames.add(region);
        }

        playerRunLeft = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        for(int i=5 ; i<6 ; i++) {
            TextureRegion region = new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT);
            region.flip(true, false);
            frames.add(region);
        }

        playerJumpLeft = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        TextureRegion region = new TextureRegion(texture, 0, 0, WIDTH, HEIGHT);
        region.flip(true, false);
        frames.add(region);

        playerStandingLeft = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        region = new TextureRegion(texture, 7 * WIDTH, 0, WIDTH, HEIGHT);
        region.flip(true, false);
        frames.add(region);

        playerFlipLeft = new Animation(0.1f, frames);
        frames.clear();
    }

    private void initRightAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();

        ///////////////////////////

        for(int i=1 ; i<4 ; i++)
            frames.add(new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT));

        playerRunRight = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        for(int i=5 ; i<6 ; i++)
            frames.add(new TextureRegion(texture, i * WIDTH, 0, WIDTH, HEIGHT));

        playerJumpRight = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        frames.add(new TextureRegion(texture, 0, 0, WIDTH, HEIGHT));

        playerStandingRight = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        frames.add(new TextureRegion(texture, 6 * WIDTH, 0, WIDTH, HEIGHT));

        playerDie = new Animation(0.1f, frames);
        frames.clear();

        ///////////////////////////

        frames.add(new TextureRegion(texture, 7 * WIDTH, 0, WIDTH, HEIGHT));

        playerFlipRight = new Animation(0.1f, frames);
        frames.clear();
    }

    public void update(){
        inputHandler();

        if(!collision)
            move();

        if (this.getY() > START_Y) {
            jumpVelocity += GRAVITY;
            collision = false;
        } else {
            this.setY(START_Y);
            jumpVelocity = 0;
            jump = true;
            floor = true;
        }

        box.setPosition((int) getX(), (int) getY());
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

                case FLIP:
                    result = playerFlipRight;
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

                case FLIP:
                    result = playerFlipLeft;
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
        if (runVelocity != 0 && (jumpVelocity == 0 || floor) && !die && !flip)
            return State.RUNNING;

        else if (jumpVelocity > 0 && !die && !flip)
            return State.JUMPING;

        else if (jumpVelocity < 0 && !die && !flip)
            return State.FALLING;

        else if (flip)
            return State.FLIP;

        else if (die)
            return State.DIE;

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
            jumpVelocity += JUMP_VELOCITY;
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

    public boolean getRotate() {
        return this.rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    //////////// SETTERS

    public boolean getDie() {
        return die;
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    public Rectangle getRectangleBox() {
        return box;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public void setCollision(boolean value) {
        collision = value;
    }

    private enum State {FALLING, JUMPING, FLIP, STANDING, RUNNING, DIE}

    private enum Direction {LEFT, RIGHT}
}
