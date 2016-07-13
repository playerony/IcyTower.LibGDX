package com.mygdx.game.com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.com.mygdx.game.AssetsManager.Asset;

/**
 * Created by pawel_000 on 2016-05-25.
 */
public class Platform extends Image {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static Bricks bricks;
    private static int SIZE = 0;
    private static float posX = 0;
    private static float posY = 0;
    private boolean points = true;
    private Rectangle bounds;
    private Array<Block> blocks;
    private Texture brickTexture;
    private Texture blueBrickTexture;
    private Texture greyBrickTexture;
    private Texture greenBrickTexture;
    private Texture questionMarkTexture;
    public Platform(float x, float y, int size, Asset assets) {
        SIZE = size;

        this.setWidth(SIZE * WIDTH);
        this.setHeight(HEIGHT);
        this.setPosition(x, y);

        bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());

        initTextures(assets);
        init();
    }

    private void initTextures(Asset assets) {
        brickTexture = assets.manager.get("assets/brick.png", Texture.class);
        blueBrickTexture = assets.manager.get("assets/brick_blue.png", Texture.class);
        greenBrickTexture = assets.manager.get("assets/brick_green.png", Texture.class);
        greyBrickTexture = assets.manager.get("assets/brick_grey.png", Texture.class);

        questionMarkTexture = assets.manager.get("assets/questionMark.png", Texture.class);
    }

    private void init(){
        blocks = new Array<Block>();

        int value = 0;
        int lastValue = 0;

        for(int i=0 ; i<SIZE ; i++){
            lastValue = value;
            value = MathUtils.random(10);
            Block b = null;

            if (value == 3 && i > 0 && i < SIZE - 1 && value != lastValue)
                b = new Block(posX + (i * WIDTH), posY, WIDTH, HEIGHT, questionMarkTexture);

            else
                b = new Block(posX + (i * WIDTH), posY, WIDTH, HEIGHT, brickTexture);

            blocks.add(b);
        }
    }

    public void removeBlocks() {
        for (Block b : blocks)
            b.addAction(Actions.removeActor());

        blocks.clear();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void onStage(Stage stage){
        for(Block b : blocks)
            stage.addActor(b);
    }

    public int getSIZE(){

        return SIZE;
    }

    public void setSIZE(final int size) {
        SIZE = size;

        createNewPlatform();
        this.setWidth(SIZE * WIDTH);
    }

    private void createNewPlatform() {
        removeBlocks();

        for (int i = 0; i < SIZE; i++) {
            int value = MathUtils.random(7);
            Block b = null;

            if (value == 3 && i > 0 && i < SIZE - 1)
                b = new Block(posX + (i * WIDTH), posY, WIDTH, HEIGHT, questionMarkTexture);

            else
                b = new Block(posX + (i * WIDTH), posY, WIDTH, HEIGHT, getTexture());

            blocks.add(b);
        }
    }

    private Texture getTexture() {
        switch (bricks) {
            case RED: {
                return brickTexture;
            }

            case BLUE: {
                return blueBrickTexture;
            }

            case GREEN: {
                return greenBrickTexture;
            }

            case GREY: {
                return greyBrickTexture;
            }
        }

        return brickTexture;
    }

    public void setPos(float x, float y) {
        int i=0;

        for(Block b : blocks) {
            b.setPosition(x + WIDTH * i, y);
            i++;
        }

        bounds.setPosition(x, y);
        bounds.setSize(getWidth(), HEIGHT);
        setPosition(x, y);
    }

    public void setPosition(float x, float y){
        posX = x;
        posY = y;

        this.setX(x);
        this.setY(y);
    }

    public void setBricks(final int value) {

        if (value == 0) {
            bricks = Bricks.RED;
        } else if (value == 1) {
            bricks = Bricks.GREEN;
        } else if (value == 2) {
            bricks = Bricks.BLUE;
        } else {
            bricks = Bricks.GREY;
        }
    }

    public boolean isPoints() {
        return points;
    }

    public void setPoints(boolean points) {
        this.points = points;
    }

    private enum Bricks {RED, GREEN, GREY, BLUE}
}
